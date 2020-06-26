package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.core.http.model.ResponseStatus.UNSUPPORTED_MEDIA_TYPE
import acsgh.mad.scala.server.router.http.body.reader.HttpBodyReader
import acsgh.mad.scala.server.router.http.convertions.{HttpDefaultFormats, HttpDefaultParamHandling}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpRequestBody extends HttpDefaultParamHandling
  with HttpDefaultFormats
  with HttpRequestHeaderDirectives
  with HttpRouteDirectives {

  def requestBody(action: Array[Byte] => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = action(context.request.bodyBytes)

  def requestBody[T](action: T => HttpResponse)(implicit context: HttpRequestContext, reader: HttpBodyReader[T]): HttpResponse = {
    if (reader.strictContentTypes) {
      requestHeader("Content-Type") { contentType =>

        if (!context.request.validContentType(reader.contentTypes, contentType)) {
          error(UNSUPPORTED_MEDIA_TYPE)
        } else {
          action(reader.read(context.request.bodyBytes))
        }

      }
    } else {
      action(reader.read(context.request.bodyBytes))
    }
  }
}
