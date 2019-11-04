package acsgh.mad.scala.router.http

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import acsgh.mad.scala.router.http.listener.RequestListener
import acsgh.mad.scala.router.http.model.{Route, _}
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.{StopWatch, TimerSplitter}
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup

import scala.concurrent.TimeoutException


final class HttpRouter(serverName: => String, _productionMode: => Boolean, workerThreads: => Int, workerTimeoutSeconds: => Int) extends LogSupport {

  private var filters: List[Route[FilterAction]] = List()
  private var servlet: List[Route[RouteAction]] = List()
  private val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  private val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  private val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler(_productionMode)
  private var _requestListeners: List[RequestListener] = List()
  private lazy val handlersGroup: EventLoopGroup = new NioEventLoopGroup(workerThreads)

  private val _started = new AtomicBoolean(false)

  def productionMode: Boolean = _productionMode

  def requestListeners: List[RequestListener] = _requestListeners

  def addRequestListeners(listener: RequestListener): Unit = {
    _requestListeners = _requestListeners ++ List(listener)
  }

  def removeRequestListeners(listener: RequestListener): Unit = {
    _requestListeners = _requestListeners.filterNot(_ == listener)
  }

  def started: Boolean = _started.get()

  def start(): Unit = {
    if (_started.compareAndSet(false, true)) {
    }
  }

  def stop(): Unit = {
    if (_started.compareAndSet(true, false)) {
      handlersGroup.shutdownGracefully
    }
  }

  private[scala] def process(httpRequest: Request): Response = {
    implicit val ctx: RequestContext = getRequestContext(httpRequest)
    onStart()
    try {
      if (workerTimeoutSeconds > 0) {
        val result = handlersGroup.submit(() => runSafe(runServlet))
        try {
          result.get(workerTimeoutSeconds, TimeUnit.SECONDS)
        } catch {
          case e: TimeoutException =>
            result.cancel(true)
            onTimeout()
            getErrorResponse(ResponseStatus.INTERNAL_SERVER_ERROR, Some("Request Timeout"))
          case e: Exception =>
            onException(e)
            exceptionHandler.handle(e)
        }
      } else {
        runSafe(runServlet)
      }
    } finally {
      onStop()
    }
  }

  private[http] def servlet(route: Route[RouteAction]): Unit = {
    checkNotStarted()
    servlet = servlet ++ List(route)
  }

  private[http] def filter(route: Route[FilterAction]): Unit = {
    checkNotStarted()
    filters = filters ++ List(route)
  }

  private[http] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: RequestContext): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(responseStatus, message)
  }

  private def runServlet(context: RequestContext): Response = {
    checkStarted()
    servlet
      .find(_.canApply(context.request))
      .map(r => runRoute(r, context))
      .getOrElse({
        getErrorResponse(ResponseStatus.NOT_FOUND)(context)
      })
  }

  private def runRoute(route: Route[RouteAction], context: RequestContext): Response = {
    val stopWatch = StopWatch.createStarted()
    try {
      implicit val ctx: RequestContext = context.ofRoute(route)
      val filtersToExecute = filters.filter(_.canApply(context.request))
      runFilters(route, filtersToExecute)
    } finally {
      stopWatch.printElapseTime("Servlet " + route.methods + " " + route.uri, log, LogLevel.TRACE)
    }
  }

  private def runFilters(route: Route[RouteAction], nextFilters: List[Route[FilterAction]])(implicit context: RequestContext): Response = {
    runSafe { c1 =>
      if (nextFilters.nonEmpty) {
        val currentFilter = nextFilters.head
        log.trace("Filter {} {}", Array(currentFilter.methods, currentFilter.uri): _*)
        val stopWatch = StopWatch.createStarted()
        try {
          currentFilter.action(c1)(_ => runFilters(route, nextFilters.tail))
        } finally {
          stopWatch.printElapseTime("Filter " + currentFilter.methods + " " + currentFilter.uri, log, LogLevel.TRACE)
        }
      } else {
        runSafe(route.action)
      }
    }
  }


  private def runSafe(action: RequestContext => Response)(implicit ctx: RequestContext): Response = {
    try {
      action(ctx)
    } catch {
      case _: InterruptedException =>
        onTimeout()
        getErrorResponse(ResponseStatus.INTERNAL_SERVER_ERROR, Some("Request Timeout"))
      case e: Exception =>
        onException(e)
        exceptionHandler.handle(e)
    }
  }


  private def onTimeout()(implicit ctx: RequestContext): Unit = {
    log.trace(s"Timeout during request:  ${ctx.request.method} ${ctx.request.uri}")
    notify(_.onTimeout())
  }

  private def onException(e: Exception)(implicit ctx: RequestContext): Unit = {
    log.trace(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", e)
    notify(_.onException(e))
  }

  private def onStart()(implicit ctx: RequestContext): Unit = {
    log.trace(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
    notify(_.onStart())
  }

  private def onStop()(implicit ctx: RequestContext): Unit = {
    log.trace(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code} in ${TimerSplitter.getIntervalInfo(System.currentTimeMillis() - ctx.request.starTime, TimeUnit.MILLISECONDS)}")
    notify(_.onStop())
  }

  private def getRequestContext(httpRequest: Request): RequestContext = {
    val ctx: RequestContext = model.RequestContext(httpRequest, ResponseBuilder(httpRequest), this)
    ctx.response.header("Server", serverName)
    ctx
  }

  private def notify(action: RequestListener => Unit): Unit = {
    _requestListeners.foreach(action)
  }

  private def checkNotStarted(): Unit = {
    if (started) {
      throw new IllegalArgumentException("This action can only be performed before start the service")
    }
  }

  private def checkStarted(): Unit = {
    if (!started) {
      throw new IllegalArgumentException("This action can only be performed after start the service")
    }
  }
}
