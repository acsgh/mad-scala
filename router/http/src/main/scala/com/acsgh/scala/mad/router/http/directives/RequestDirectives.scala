package com.acsgh.scala.mad.router.http.directives

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acsgh.scala.mad.router.http.model.Response
import com.acsgh.scala.mad.router.http.model.ResponseStatus.UNSUPPORTED_MEDIA_TYPE

trait RequestDirectives extends DefaultParamHandling with DefaultFormats with RequestParamsDirectives with RequestHeaderDirectives with RequestQueryDirectives with RequestCookieDirectives with RouteDirectives {

  def requestBody(action: Array[Byte] => Response)(implicit context: RequestContext): Response = action(context.request.bodyBytes)

  def requestBody[T](action: T => Response)(implicit context: RequestContext, reader: BodyReader[T]): Response = {
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
