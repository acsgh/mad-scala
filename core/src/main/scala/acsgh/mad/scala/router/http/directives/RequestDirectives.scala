package acsgh.mad.scala.router.http.directives

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.router.http.model.{Response, RouteResult}
import acsgh.mad.scala.router.http.model.ResponseStatus.UNSUPPORTED_MEDIA_TYPE

trait RequestDirectives extends DefaultParamHandling with DefaultFormats with RequestParamsDirectives with RequestHeaderDirectives with RequestQueryDirectives with RequestCookieDirectives with RouteDirectives {

  def requestBody(action: Array[Byte] => RouteResult)(implicit context: RequestContext): RouteResult = action(context.request.bodyBytes)

  def requestBody[T](action: T => RouteResult)(implicit context: RequestContext, reader: BodyReader[T]): RouteResult = {
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
