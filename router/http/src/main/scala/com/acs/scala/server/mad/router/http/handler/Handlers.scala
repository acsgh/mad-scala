package com.acs.scala.server.mad.router.http.handler

import com.acs.scala.server.mad.router.http.RequestContext
import com.acs.scala.server.mad.router.http.directives.Directives
import com.acs.scala.server.mad.router.http.model.{Response, ResponseStatus}

trait ErrorCodeHandler extends Directives {
  def handle(responseStatus: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): Response
}

trait ExceptionHandler extends Directives {
  def handle(throwable: Throwable)(implicit requestContext: RequestContext): Response
}
