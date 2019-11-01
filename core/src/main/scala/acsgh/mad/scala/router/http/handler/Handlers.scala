package acsgh.mad.scala.router.http.handler

import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.model.{RequestContext, Response, ResponseStatus}

trait ErrorCodeHandler extends Directives {
  def handle(responseStatus: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): Response
}

trait ExceptionHandler extends Directives {
  def handle(throwable: Throwable)(implicit requestContext: RequestContext): Response
}
