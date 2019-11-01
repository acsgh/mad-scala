package acsgh.mad.scala.router.http.directives

import acsgh.mad.scala.router.http.convertions.{BodyWriter, DefaultFormats, DefaultParamHandling, ParamWriter}
import acsgh.mad.scala.router.http.model.{HttpCookie, ProtocolVersion, RequestContext, Response, ResponseStatus}

trait ResponseDirectives extends DefaultParamHandling with DefaultFormats {

  def responseHeader[T](name: String, value: T)(action: => Response)(implicit context: RequestContext, converter: ParamWriter[T]): Response = {
    context.response.header(name, converter.write(value))
    action
  }

  def responseStatus(input: ResponseStatus)(action: => Response)(implicit context: RequestContext): Response = {
    context.response.status(input)
    action
  }

  def responseCookie(input: HttpCookie)(action: => Response)(implicit context: RequestContext): Response = {
    responseHeader("Set-Cookie", input.toValue)(action)
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
