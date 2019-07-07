package com.acs.scala.server.mad.router.ws.handler

import com.acs.scala.server.mad.router.ws.WSRequestContext
import com.acs.scala.server.mad.router.ws.model.WSResponse

class DefaultHandler extends WSHandler {
  override def handle(implicit context: WSRequestContext): WSResponse = responseBody("Unknown route")
}
