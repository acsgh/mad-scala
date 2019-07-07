package com.acsgh.scala.mad.router.http

import com.acsgh.scala.mad.URLSupport
import com.acsgh.scala.mad.router.http.model.{Request, RequestMethod}

private[http] case class HttpRoute[T](uri: String, methods: Set[RequestMethod], handler: T) extends URLSupport {

  private[http] def canApply(request: Request): Boolean = validMethod(request) && matchUrl(uri, request.uri)

  private def validMethod(request: Request): Boolean = methods.isEmpty || methods.contains(request.method)
}
