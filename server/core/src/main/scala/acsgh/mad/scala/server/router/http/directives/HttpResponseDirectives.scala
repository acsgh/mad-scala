package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.{HttpCookie, HttpResponse, ProtocolVersion, ResponseStatus}
import acsgh.mad.scala.server.router.http.body.writer.HttpBodyWriter
import acsgh.mad.scala.server.router.http.body.writer.default._
import acsgh.mad.scala.server.router.http.convertions.{HttpDefaultFormats, HttpDefaultParamHandling, HttpParamWriter}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpResponseDirectives extends HttpDefaultParamHandling with HttpDefaultFormats {

  def responseHeader[T](name: String, value: T)(action: => HttpResponse)(implicit context: HttpRequestContext, converter: HttpParamWriter[T]): HttpResponse = {
    context.response.header(name, converter.write(value))
    action
  }

  def responseStatus(input: ResponseStatus)(action: => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    context.response.status(input)
    action
  }

  def responseCookie(input: HttpCookie)(action: => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    responseHeader("Set-Cookie", input.toValue)(action)
  }

  def responseVersion(input: ProtocolVersion)(action: => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    context.response.version(input)
    action
  }

  def responseBody(input: Array[Byte])(implicit context: HttpRequestContext): HttpResponse = context.response.body(input)

  def responseBody[T](input: T)(implicit context: HttpRequestContext, writer: HttpBodyWriter[T]): HttpResponse = {
    if (!context.response.hasHeader("Content-Type")) {
      responseHeader("Content-Type", writer.contentType) {
        responseBody(writer.write(input))
      }
    } else {
      responseBody(writer.write(input))
    }
  }

  def noBody(status: ResponseStatus = ResponseStatus.NO_CONTENT)(implicit context: HttpRequestContext): HttpResponse = {
    responseStatus(status) {
      responseBody("")
    }
  }
}
