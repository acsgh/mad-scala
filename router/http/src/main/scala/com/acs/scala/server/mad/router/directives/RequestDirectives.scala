package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.exception.UnexpectedContentTypeException
import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.model.Response

trait RequestDirectives extends DefaultParamHandling with DefaultFormats {

  def requestQueryParam(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.queryParams(name).head
    action(param)
  }

  def requestHeader(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.headers(name).head
    action(param)
  }

  def requestBody(action: Array[Byte] => Response)(implicit context: RequestContext): Response = action(context.request.bodyBytes)

  def requestBody[T](action: T => Response)(implicit context: RequestContext, reader: BodyReader[T]): Response = {
    requestHeader("Content-Type") { contentType =>

      if (!context.request.validContentType(reader.contentTypes, contentType)) {
        throw UnexpectedContentTypeException(contentType)
      }

      action(reader.read(context.request.bodyBytes))
    }
  }
}
