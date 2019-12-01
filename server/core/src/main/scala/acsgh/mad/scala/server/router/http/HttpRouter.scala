package acsgh.mad.scala.server.router.http

import java.util.concurrent.TimeUnit

import acsgh.mad.scala.core.http.model.{HttpRequest, HttpResponse, HttpResponseBuilder, ResponseStatus}
import acsgh.mad.scala.server.router.http.handler.{ErrorCodeHandler, ExceptionHandler}
import acsgh.mad.scala.server.router.http.listener.RequestListener
import acsgh.mad.scala.server.router.http.model.{Route, _}
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.{StopWatch, TimerSplitter}
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup

import scala.concurrent.TimeoutException

case class HttpRouter
(
  serverName: String,
  productionMode: Boolean,
  workerThreads: Int,
  private val workerTimeoutSeconds: Int,
  private val filters: List[Route[FilterAction]],
  private val servlet: List[Route[RouteAction]],
  private val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler],
  private val defaultErrorCodeHandler: ErrorCodeHandler,
  private val exceptionHandler: ExceptionHandler,
  private val requestListeners: List[RequestListener]
) extends LogSupport {

  private val handlersGroup: EventLoopGroup = new NioEventLoopGroup(workerThreads)

  def close(): Unit = {
    handlersGroup.shutdownGracefully
  }

  private[scala] def process(httpRequest: HttpRequest): HttpResponse = {

    implicit val ctx: HttpRequestContext = getRequestContext(httpRequest)
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


  private[http] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: HttpRequestContext): HttpResponse = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(responseStatus, message)
  }

  private def runServlet(context: HttpRequestContext): HttpResponse = {
    implicit val ctx: HttpRequestContext = context
    context
      .route
      .map(r => runRoute(r))
      .getOrElse({
        getErrorResponse(ResponseStatus.NOT_FOUND)(context)
      })
  }

  private def runRoute(route: Route[RouteAction])(implicit context: HttpRequestContext): HttpResponse = {
    val stopWatch = StopWatch.createStarted()
    try {
      val filtersToExecute = filters.filter(_.canApply(context.request))
      runFilters(route, filtersToExecute)
    } finally {
      stopWatch.printElapseTime("Servlet " + route.methods + " " + route.uri, log, LogLevel.TRACE)
    }
  }

  private def runFilters(route: Route[RouteAction], nextFilters: List[Route[FilterAction]])(implicit context: HttpRequestContext): HttpResponse = {
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


  private def runSafe(action: HttpRequestContext => HttpResponse)(implicit ctx: HttpRequestContext): HttpResponse = {
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


  private def onTimeout()(implicit ctx: HttpRequestContext): Unit = {
    log.trace(s"Timeout during request:  ${ctx.request.method} ${ctx.request.uri}")
    notify(_.onTimeout())
  }

  private def onException(e: Exception)(implicit ctx: HttpRequestContext): Unit = {
    log.trace(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", e)
    notify(_.onException(e))
  }

  private def onStart()(implicit ctx: HttpRequestContext): Unit = {
    log.trace(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
    notify(_.onStart())
  }

  private def onStop()(implicit ctx: HttpRequestContext): Unit = {
    log.trace(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code} in ${TimerSplitter.getIntervalInfo(System.currentTimeMillis() - ctx.request.starTime, TimeUnit.MILLISECONDS)}")
    notify(_.onStop())
  }

  private def getRequestContext(httpRequest: HttpRequest): HttpRequestContext = {
    val route = servlet
      .find(_.canApply(httpRequest))

    val ctx: HttpRequestContext = model.HttpRequestContext(httpRequest, HttpResponseBuilder.from(httpRequest), this, route)
    ctx.response.header("Server", serverName)
    ctx
  }

  private def notify(action: RequestListener => Unit): Unit = {
    requestListeners.foreach(action)
  }
}
