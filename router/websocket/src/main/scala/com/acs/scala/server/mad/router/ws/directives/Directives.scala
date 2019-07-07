package com.acs.scala.server.mad.router.ws.directives

import com.acs.scala.server.mad.router.ws.WSRequestContext
import com.acs.scala.server.mad.router.ws.convertions.DefaultFormats
import com.acs.scala.server.mad.router.ws.model.WSResponse

trait Directives extends DefaultFormats{
  def responseBody(input: Array[Byte])(implicit context: WSRequestContext): WSResponse = context.response.bytes(input)

  def responseBody(input: String)(implicit context: WSRequestContext): WSResponse = context.response.text(input)
}
