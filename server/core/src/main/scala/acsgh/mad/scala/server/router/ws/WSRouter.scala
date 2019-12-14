package acsgh.mad.scala.server.router.ws

import java.util.concurrent.TimeUnit

import acsgh.mad.scala.core.WorkerExecutor
import acsgh.mad.scala.core.ws.model._
import acsgh.mad.scala.server.router.ws.listener.WSRequestListener
import acsgh.mad.scala.server.router.ws.model._
import com.acsgh.common.scala.log.LogSupport
import com.acsgh.common.scala.time.TimerSplitter

import scala.concurrent.TimeoutException

case class WSRouter
(
  serverName: String,
  productionMode: Boolean,
  workerThreads: Int,
  private val workerTimeoutSeconds: Int,
  wsRoutes: Map[String, WSRoute],
  private val defaultHandler: WSRouteAction,
  private val requestListeners: List[WSRequestListener]
) extends LogSupport {

  private val handlersGroup: WorkerExecutor = WorkerExecutor("ws-workers", workerThreads)

  def close(): Unit = {
    handlersGroup.shutdownGracefully()
  }

  private[scala] def process(request: WSRequest): Option[WSResponse] = {
    implicit val ctx: WSRequestContext = WSRequestContext(request, wsRoutes.get(request.uri.toString))
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
    requestListeners.foreach(action)
  }
}
