package com.acsgh.scala.mad.router.ws.handler

import com.acsgh.scala.mad.LogSupport
import com.acsgh.scala.mad.router.ws.WSRequestContext
import com.acsgh.scala.mad.router.ws.directives.Directives
import com.acsgh.scala.mad.router.ws.model.WSResponse

trait WSHandler extends LogSupport with Directives {
  def handle(implicit requestContext: WSRequestContext): Option[WSResponse]
}
