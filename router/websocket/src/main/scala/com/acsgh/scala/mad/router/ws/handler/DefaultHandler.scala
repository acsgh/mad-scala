package com.acsgh.scala.mad.router.ws.handler

import com.acsgh.scala.mad.router.ws.WSRequestContext
import com.acsgh.scala.mad.router.ws.model.WSResponse

class DefaultHandler extends WSHandler {
  override def handle(implicit context: WSRequestContext): Option[WSResponse] = responseBody("Unknown route")
}
