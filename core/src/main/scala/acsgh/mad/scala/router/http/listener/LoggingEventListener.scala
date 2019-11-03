package acsgh.mad.scala.router.http.listener

import java.util.concurrent.TimeUnit

import acsgh.mad.scala.router.http.model.RequestContext
import com.acsgh.common.scala.time.TimerSplitter

case object LoggingEventListener extends RequestListener {

  def onStart()(implicit ctx: RequestContext): Unit = {
    log.debug(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
  }

  def onStop()(implicit ctx: RequestContext): Unit = {
    val duration = System.currentTimeMillis() - ctx.request.starTime

    log.info(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code} in ${TimerSplitter.getIntervalInfo(duration, TimeUnit.MILLISECONDS)}")
  }

  override def onTimeout()(implicit ctx: RequestContext): Unit = {
    log.error(s"Timeout during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'")
  }

  def onException(exception: Exception)(implicit ctx: RequestContext): Unit = {
    log.error(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", exception)
  }
}
