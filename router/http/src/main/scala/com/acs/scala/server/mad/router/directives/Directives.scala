package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.constant.{RedirectStatus, ResponseStatus}
import com.acs.scala.server.mad.router.{RequestContext, Response}

trait Directives extends DefaultParamHandling {

  def queryParam(name: String)(action: String => Response)(implicit context: RequestContext): Response = {
    val param = context.request.queryParams.get(name)
    action(param)
  }

  def responseHeader[T](name: String, value: T)(action: => Response)(implicit context: RequestContext, converter: ParamWriter[T]): Response = {
    context.response.header(name, converter.write(value))
    action
  }

  def redirect(url: String, redirectStatus: RedirectStatus = RedirectStatus.FOUND)(implicit context: RequestContext): Response = {
    responseHeader("Location", url) {
      context.router.getErrorResponse(context, redirectStatus.status)
    }
  }

  def error(status: ResponseStatus = ResponseStatus.INTERNAL_SERVER_ERROR)(implicit context: RequestContext): Response = {
    context.router.getErrorResponse(context, status)
  }

  def serve(url: String)(implicit context: RequestContext): Response = context.router.process(context.request.ofUri(url))
}
