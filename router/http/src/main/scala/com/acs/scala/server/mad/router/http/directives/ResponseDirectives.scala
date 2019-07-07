package com.acs.scala.server.mad.router.http.directives

import com.acs.scala.server.mad.router.http.RequestContext
import com.acs.scala.server.mad.router.http.model.{ProtocolVersion, Response, ResponseStatus}
import com.acs.scala.server.mad.router.http.convertions.{BodyWriter, DefaultFormats, DefaultParamHandling, ParamWriter}

trait ResponseDirectives extends DefaultParamHandling with DefaultFormats {

  def responseHeader[T](name: String, value: T)(action: => Response)(implicit context: RequestContext, converter: ParamWriter[T]): Response = {
    context.response.header(name, converter.write(value))
    action
  }

  def responseStatus(input: ResponseStatus)(action: => Response)(implicit context: RequestContext): Response = {
    context.response.status(input)
    action
  }

  def responseVersion(input: ProtocolVersion)(action: => Response)(implicit context: RequestContext): Response = {
    context.response.version(input)
    action
  }

  def responseBody(input: Array[Byte])(implicit context: RequestContext): Response = context.response.body(input)

  def responseBody[T](input: T)(implicit context: RequestContext, writer: BodyWriter[T]): Response = {
    if (!context.response.hasHeader("Content-Type")) {
      responseHeader("Content-Type", writer.contentType) {
        responseBody(writer.write(input))
      }
    } else {
      responseBody(writer.write(input))
    }
  }
}
