package acsgh.mad.scala.router.ws.listener

import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.ws.model.{WSRequestContext, WSResponse}

trait WSRequestListener extends Directives {
  def onStart()(implicit requestContext: WSRequestContext): Unit

  def onStop(response: Option[WSResponse])(implicit requestContext: WSRequestContext): Unit

  def onTimeout()(implicit requestContext: WSRequestContext): Unit

  def onException(exception: Exception)(implicit requestContext: WSRequestContext): Unit
}