package acsgh.mad.scala.server.router.http.directives

import java.net.URI

import acsgh.mad.scala.core.http.model.{HttpResponse, RedirectStatus, ResponseStatus}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpRouteDirectives extends HttpResponseDirectives {

  def redirect(url: String, redirectStatus: RedirectStatus = RedirectStatus.FOUND)(implicit context: HttpRequestContext): HttpResponse = {
    responseHeader("Location", url) {
      context.router.getErrorResponse(redirectStatus.status)
    }
  }

  def error(status: ResponseStatus = ResponseStatus.INTERNAL_SERVER_ERROR, message: Option[String] = None)(implicit context: HttpRequestContext): HttpResponse = {
    context.router.getErrorResponse(status, message)
  }

  def serve(url: String)(implicit context: HttpRequestContext): HttpResponse = context.router.process(context.request.copy(uri = URI.create(url)))
}
