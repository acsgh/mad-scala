package acsgh.mad.scala.router.http.directives

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.convertions.{BodyWriter, DefaultFormats, DefaultParamHandling, ParamWriter}
import acsgh.mad.scala.router.http.model.{HttpCookie, ProtocolVersion, ResponseStatus, RouteResult}

trait ResponseDirectives extends DefaultParamHandling with DefaultFormats {

  def responseHeader[T](name: String, value: T)(action: => RouteResult)(implicit context: RequestContext, converter: ParamWriter[T]): RouteResult = {
    context.response.header(name, converter.write(value))
    action
  }

  def responseStatus(input: ResponseStatus)(action: => RouteResult)(implicit context: RequestContext): RouteResult = {
    context.response.status(input)
    action
  }

  def responseCookie(input: HttpCookie)(action: => RouteResult)(implicit context: RequestContext): RouteResult = {
    responseHeader("Set-Cookie", input.toValue)(action)
  }

  def responseVersion(input: ProtocolVersion)(action: => RouteResult)(implicit context: RequestContext): RouteResult = {
    context.response.version(input)
    action
  }

  def responseBody(input: Array[Byte])(implicit context: RequestContext): RouteResult = context.response.body(input)

  def responseBody[T](input: T)(implicit context: RequestContext, writer: BodyWriter[T]): RouteResult = {
    if (!context.response.hasHeader("Content-Type")) {
      responseHeader("Content-Type", writer.contentType) {
        responseBody(writer.write(input))
      }
    } else {
      responseBody(writer.write(input))
    }
  }
}
