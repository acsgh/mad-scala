package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.core.http.model.ResponseStatus.UNSUPPORTED_MEDIA_TYPE
import acsgh.mad.scala.server.router.http.body.reader.HttpBodyReader
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.HttpDefaultParamHandling
import acsgh.mad.scala.server.router.http.params.reader.default._

object HttpRequestBody extends HttpRequestBody

trait HttpRequestBody extends HttpDefaultParamHandling
  with HttpDirectivesBase
  with HttpRequestHeaderDirectives
  with HttpRouteDirectives {

  def requestBody(action: Array[Byte] => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = action(context.request.bodyBytes)

  def requestBody[T](action: T => HttpResponse)(implicit context: HttpRequestContext, reader: HttpBodyReader[T]): HttpResponse = {
    def read() = action(reader.read(context.request.bodyBytes))

    if (reader.strictContentTypes) {
      requestHeader("Content-Type") { contentType =>

        if (!context.request.validContentType(reader.contentTypes, contentType)) {
          error(UNSUPPORTED_MEDIA_TYPE)
        } else {
          read()
        }
      }
    } else {
      read()
    }
  }

  def requestBodyOpt[T, O](action: Option[T] => O)(implicit context: HttpRequestContext, reader: HttpBodyReader[T]): O = {
    def nop() = action(None)

    def read() = try {
      action(Option(reader.read(context.request.bodyBytes)))
    } catch {
      case _: Exception => nop()
    }

    if (reader.strictContentTypes) {
      requestHeader("Content-Type") { contentType =>

        if (!context.request.validContentType(reader.contentTypes, contentType)) {
          nop()
        } else {
          read()
        }
      }
    } else {
      read()
    }
  }
}
