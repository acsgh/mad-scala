package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.core.http.model.ResponseStatus.UNSUPPORTED_MEDIA_TYPE
import acsgh.mad.scala.server.router.http.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpRequestDirectives extends DefaultParamHandling with DefaultFormats with HttpRequestParamsDirectives with HttpRequestHeaderDirectives with HttpRequestQueryDirectives with HttpRequestCookieDirectives with HttpRouteDirectives {

  def requestBody(action: Array[Byte] => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = action(context.request.bodyBytes)

  def requestBody[T](action: T => HttpResponse)(implicit context: HttpRequestContext, reader: BodyReader[T]): HttpResponse = {
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
