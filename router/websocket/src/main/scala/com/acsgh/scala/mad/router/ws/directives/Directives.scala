package com.acsgh.scala.mad.router.ws.directives

import com.acsgh.scala.mad.router.ws.WSRequestContext
import com.acsgh.scala.mad.router.ws.convertions.DefaultFormats
import com.acsgh.scala.mad.router.ws.model.WSResponse

trait Directives extends DefaultFormats{
  def responseBody(input: Array[Byte])(implicit context: WSRequestContext): WSResponse = context.response.bytes(input)

  def responseBody(input: String)(implicit context: WSRequestContext): WSResponse = context.response.text(input)
}
