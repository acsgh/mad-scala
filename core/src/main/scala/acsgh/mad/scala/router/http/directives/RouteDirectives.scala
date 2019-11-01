package acsgh.mad.scala.router.http.directives

import java.net.URI

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.model.{RedirectStatus, Response, ResponseStatus, RouteResult}

trait RouteDirectives extends ResponseDirectives {

  def redirect(url: String, redirectStatus: RedirectStatus = RedirectStatus.FOUND)(implicit context: RequestContext): RouteResult = {
    responseHeader("Location", url) {
      context.router.getErrorResponse(redirectStatus.status)
    }
  }

  def error(status: ResponseStatus = ResponseStatus.INTERNAL_SERVER_ERROR, message: Option[String] = None)(implicit context: RequestContext): RouteResult = {
    context.router.getErrorResponse(status, message)
  }

  def serve(url: String)(implicit context: RequestContext): RouteResult = context.router.process(context.request.copy(uri = URI.create(url)))
}
