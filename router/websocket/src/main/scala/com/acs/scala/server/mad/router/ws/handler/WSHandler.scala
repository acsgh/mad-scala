package com.acs.scala.server.mad.router.ws.handler

import com.acs.scala.server.mad.LogSupport
import com.acs.scala.server.mad.router.ws.WSRequestContext
import com.acs.scala.server.mad.router.ws.directives.Directives
import com.acs.scala.server.mad.router.ws.model.WSResponse

trait WSHandler extends LogSupport with Directives {
  def handle(implicit requestContext: WSRequestContext): WSResponse
}
