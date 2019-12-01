package acsgh.mad.scala.server.router.ws.listener

import java.util.concurrent.TimeUnit

import acsgh.mad.scala.core.ws.model.{WSRequest, WSRequestBinary, WSRequestText, WSResponse}
import acsgh.mad.scala.server.router.ws.model._
import com.acsgh.common.scala.time.TimerSplitter

case object WSLoggingEventListener extends WSRequestListener {

  def onStart()(implicit ctx: WSRequestContext): Unit = {
    log.debug(s"WS Request:  ${requestType(ctx.request)} ${ctx.request.uri}")
  }

  def onStop(response: Option[WSResponse])(implicit ctx: WSRequestContext): Unit = {
    val duration = System.currentTimeMillis() - ctx.request.starTime

    log.info(s"WS Response: ${requestType(ctx.request)} ${ctx.request.uri} with ${response.map(responseType)} in ${TimerSplitter.getIntervalInfo(duration, TimeUnit.MILLISECONDS)}")
  }

  override def onTimeout()(implicit ctx: WSRequestContext): Unit = {
    log.error(s"WS Timeout during request: ${requestType(ctx.request)}: ${ctx.request.uri} - Body: '${body(ctx.request)}'")
  }

  def onException(exception: Exception)(implicit ctx: WSRequestContext): Unit = {
    log.error(s"WS Error during request: ${requestType(ctx.request)}: ${ctx.request.uri} - Body: '${body(ctx.request)}'", exception)
  }

  private def requestType(req: WSRequest): String = req.getClass.getSimpleName.replace("WSRequest", "")

  private def responseType(req: WSResponse): String = req.getClass.getSimpleName.replace("WSResponse", "")

  private def body(req: WSRequest): String = req match {
    case v: WSRequestText => v.text
    case v: WSRequestBinary => s"Bytes(${v.bytes.length}"
    case _ => ""
  }
}
