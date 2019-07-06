package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.{RequestContext, Response}

trait Directives {

  def queryParam(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.queryParams.get(name)
    action(param)
  }

  def responseHeader[T](name: String, value:T)(action: => Response)(implicit context: RequestContext, converter:ParamWriter[T]): Response = {
    context.responseBuilder.header(name, converter.write(value))
    action
  }
}
