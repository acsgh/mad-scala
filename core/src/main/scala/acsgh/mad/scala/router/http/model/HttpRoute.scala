package acsgh.mad.scala.router.http.model

import acsgh.mad.scala.URLSupport

private[http] case class HttpRoute[T](uri: String, methods: Set[RequestMethod], action: T) extends URLSupport {

  private[http] def canApply(request: Request): Boolean = validMethod(request) && matchUrl(uri, request.uri)

  private def validMethod(request: Request): Boolean = methods.isEmpty || methods.contains(request.method)
}
