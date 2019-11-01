package acsgh.mad.scala.router.ws.handler

import acsgh.mad.scala.router.ws.WSRequestContext
import acsgh.mad.scala.router.ws.model.WSResponse

class DefaultHandler extends WSHandler {
  override def handle(implicit context: WSRequestContext): Option[WSResponse] = responseBody("Unknown route")
}
