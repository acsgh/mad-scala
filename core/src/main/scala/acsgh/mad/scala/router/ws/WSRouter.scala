package acsgh.mad.scala.router.ws

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.router.ws.listener.WSRequestListener
import acsgh.mad.scala.router.ws.model._
import com.acsgh.common.scala.log.LogSupport
import com.acsgh.common.scala.time.TimerSplitter
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup

import scala.concurrent.TimeoutException


final class WSRouter(serverName: => String, _productionMode: => Boolean, workerThreads: => Int, workerTimeoutSeconds: => Int) extends LogSupport {

  private var _wsRoutes: Map[String, WSRoute] = Map()
  private val defaultHandler: WSRouteAction = _ => Some(WSResponseText("Unknown route"))
  private var _requestListeners: List[WSRequestListener] = List()
  private lazy val handlersGroup: EventLoopGroup = new NioEventLoopGroup(workerThreads)

  private val _started = new AtomicBoolean(false)

  private[scala] def wsRoutes: Map[String, WSRoute] = _wsRoutes

  def productionMode: Boolean = _productionMode

  def requestListeners: List[WSRequestListener] = _requestListeners

  def addRequestListeners(listener: WSRequestListener): Unit = {
    _requestListeners = _requestListeners ++ List(listener)
  }

  def removeRequestListeners(listener: WSRequestListener): Unit = {
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

  private[ws] def route(uri: String, subprotocols: Set[String] = Set())(handler: WSRouteAction): Unit = {
    checkNotStarted()

    _wsRoutes = _wsRoutes + (uri -> WSRoute(subprotocols, handler))
  }

  private[scala] def process(request: WSRequest): Option[WSResponse] = {
    _wsRoutes.get(request.uri.toString).map(_.handler).getOrElse(defaultHandler)

    implicit val ctx: WSRequestContext = WSRequestContext(request, _wsRoutes.get(request.uri.toString))
    onStart()

    var response: Option[WSResponse] = None
    try {
      if (workerTimeoutSeconds > 0) {
        val result = handlersGroup.submit(() => runSafe())
        try {
          response = result.get(workerTimeoutSeconds, TimeUnit.SECONDS)
        } catch {
          case e: TimeoutException =>
            result.cancel(true)
            onTimeout()
          case e: Exception =>
            onException(e)
        }
      } else {
        response = runSafe()
      }
    } finally {
      onStop(response)
    }
    response
  }

  private def runSafe()(implicit ctx: WSRequestContext): Option[WSResponse] = {
    try {
      ctx.route.map(_.handler) match {
        case Some(a) => a.apply(ctx)
        case _ =>
          log.warn(s"Request have no route associated: ${ctx.request.uri}")
          defaultHandler.apply(ctx)
      }
    } catch {
      case _: InterruptedException =>
        onTimeout()
        None
      case e: Exception =>
        onException(e)
        None
    }
  }

  def onStart()(implicit ctx: WSRequestContext): Unit = {
    log.trace(s"WS Request:  ${requestType(ctx.request)} ${ctx.request.uri}")
    notify(_.onStart())
  }

  def onStop(response: Option[WSResponse])(implicit ctx: WSRequestContext): Unit = {
    val duration = System.currentTimeMillis() - ctx.request.starTime

    log.trace(s"WS Response: ${requestType(ctx.request)} ${ctx.request.uri} with ${response.map(responseType)} in ${TimerSplitter.getIntervalInfo(duration, TimeUnit.MILLISECONDS)}")
    notify(_.onStop(response))
  }

  def onTimeout()(implicit ctx: WSRequestContext): Unit = {
    log.trace(s"WS  Timeout during request: ${requestType(ctx.request)}: ${ctx.request.uri} - Body: '${body(ctx.request)}'")
    notify(_.onTimeout())
  }

  def onException(exception: Exception)(implicit ctx: WSRequestContext): Unit = {
    log.trace(s"WS  Error during request: ${requestType(ctx.request)}: ${ctx.request.uri} - Body: '${body(ctx.request)}'", exception)
    notify(_.onException(exception))
  }

  private def requestType(req: WSRequest): String = req.getClass.getSimpleName.replace("WSRequest", "")

  private def responseType(req: WSResponse): String = req.getClass.getSimpleName.replace("WSResponse", "")

  private def body(req: WSRequest): String = req match {
    case v: WSRequestText => v.text
    case v: WSRequestBinary => s"Bytes(${v.bytes.length}"
    case _ => ""
  }

  private def notify(action: WSRequestListener => Unit): Unit = {
    _requestListeners.foreach(action)
  }

  private def checkNotStarted(): Unit = {
    if (started) {
      throw new IllegalArgumentException("This action can only be performed before start the service")
    }
  }
}
