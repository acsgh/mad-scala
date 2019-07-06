package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.convertions.{DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.{RequestContext, Response}

trait RequestDirectives extends DefaultParamHandling with DefaultFormats {

  def requestQueryParam(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.queryParams.get(name)
    action(param)
  }

  def requestHeader(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.queryParams.get(name)
    action(param)
  }
}
