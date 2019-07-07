package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.model.{Response, ResponseStatus}

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
