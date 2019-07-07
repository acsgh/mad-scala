package com.acs.scala.server.mad.router.http

import com.acs.scala.server.mad.URLSupport
import com.acs.scala.server.mad.router.http.model.{Request, RequestMethod}

private[http] case class HttpRoute[T](uri: String, methods: Set[RequestMethod], handler: T) extends URLSupport {

  private[http] def canApply(request: Request): Boolean = validMethod(request) && matchUrl(uri, request.uri)

  private def validMethod(request: Request): Boolean = methods.isEmpty || methods.contains(request.method)
}
