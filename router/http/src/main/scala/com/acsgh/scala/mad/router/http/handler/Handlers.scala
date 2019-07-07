package com.acsgh.scala.mad.router.http.handler

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.model.{Response, ResponseStatus}

trait ErrorCodeHandler extends Directives {
  def handle(responseStatus: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): Response
}

trait ExceptionHandler extends Directives {
  def handle(throwable: Throwable)(implicit requestContext: RequestContext): Response
}
