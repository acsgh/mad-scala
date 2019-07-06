package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.exception.UnexpectedContentTypeException
import com.acs.scala.server.mad.router.model.Response


trait RequestDirectives extends DefaultParamHandling with DefaultFormats {

  //  def requestQueryParam(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
  //    val param = context.request.queryParams(name).head
  //    action(param)
  //  }

  def requestQuery[P1, R1](param1: Param[P1, R1])(action: R1 => Response)(implicit context: RequestContext): Response = {
    action(param1.queryValue)
  }

  def requestQuery[T1, R1, T2, R2](param1: Param[T1, R1], param2: Param[T2, R2])(action: (R1, R2) => Response)(implicit context: RequestContext): Response = {
    action(param1.queryValue, param2.queryValue)
  }

  def requestHeader[P1, R1](param1: Param[P1, R1])(action: R1 => Response)(implicit context: RequestContext): Response = {
    action(param1.headerValue)
  }

  def requestBody(action: Array[Byte] => Response)(implicit context: RequestContext): Response = action(context.request.bodyBytes)

  def requestBody[T](action: T => Response)(implicit context: RequestContext, reader: BodyReader[T]): Response = {
    requestHeader("Content-Type") { contentType =>

      if (!context.request.validContentType(reader.contentTypes, contentType)) {
        throw new UnexpectedContentTypeException(contentType)
      }

      action(reader.read(context.request.bodyBytes))
    }
  }
}
