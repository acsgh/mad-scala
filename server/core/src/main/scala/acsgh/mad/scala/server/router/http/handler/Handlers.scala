package acsgh.mad.scala.server.router.http.handler

import acsgh.mad.scala.core.http.model.{HttpResponse, ResponseStatus}
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait ErrorCodeHandler extends HttpDirectives {
  def handle(responseStatus: ResponseStatus, message: Option[String])(implicit requestContext: HttpRequestContext): HttpResponse
}

trait ExceptionHandler extends HttpDirectives {
  def handle(throwable: Throwable)(implicit requestContext: HttpRequestContext): HttpResponse
}
