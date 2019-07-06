package com.acs.scala.server.mad.router.directives

import java.net.URI

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.model.{RedirectStatus, Response, ResponseStatus}

trait RouteDirectives extends ResponseDirectives {

  def redirect(url: String, redirectStatus: RedirectStatus = RedirectStatus.FOUND)(implicit context: RequestContext): Response = {
    responseHeader("Location", url) {
      context.router.getErrorResponse(redirectStatus.status)
    }
  }

  def error(status: ResponseStatus = ResponseStatus.INTERNAL_SERVER_ERROR, message: Option[String] = None)(implicit context: RequestContext): Response = {
    context.router.getErrorResponse(status, message)
  }

  def serve(url: String)(implicit context: RequestContext): Response = context.router.process(context.request.copy(uri = URI.create(url)))
}
