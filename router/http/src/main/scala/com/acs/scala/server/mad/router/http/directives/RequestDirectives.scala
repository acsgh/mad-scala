package com.acs.scala.server.mad.router.http.directives

import com.acs.scala.server.mad.router.http.RequestContext
import com.acs.scala.server.mad.router.http.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.http.model.{Response, ResponseStatus}

trait RequestDirectives extends DefaultParamHandling with DefaultFormats with RequestParamsDirectives with RequestHeaderDirectives with RequestQueryDirectives with RouteDirectives {

  def requestBody(action: Array[Byte] => Response)(implicit context: RequestContext): Response = action(context.request.bodyBytes)

  def requestBody[T](action: T => Response)(implicit context: RequestContext, reader: BodyReader[T]): Response = {
    requestHeader("Content-Type") { contentType =>

      if (!context.request.validContentType(reader.contentTypes, contentType)) {
        error(ResponseStatus.UNSUPPORTED_MEDIA_TYPE)
      } else {
        action(reader.read(context.request.bodyBytes))
      }

    }
  }
}
