package acsgh.mad.scala.server.router.ws.listener

import acsgh.mad.scala.core.ws.model.WSResponse
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.ws.model.WSRequestContext

trait WSRequestListener extends HttpDirectives {
  def onStart()(implicit requestContext: WSRequestContext): Unit

  def onStop(response: Option[WSResponse])(implicit requestContext: WSRequestContext): Unit

  def onTimeout()(implicit requestContext: WSRequestContext): Unit

  def onException(exception: Exception)(implicit requestContext: WSRequestContext): Unit
}