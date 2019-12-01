package acsgh.mad.scala.server.router.http.model

import acsgh.mad.scala.core.URLSupport
import acsgh.mad.scala.core.http.model.{HttpRequest, HttpResponseBuilder}
import acsgh.mad.scala.server.router.http.HttpRouter

case class HttpRequestContext
(
  request: HttpRequest,
  response: HttpResponseBuilder,
  router: HttpRouter,
  route: Option[Route[RouteAction]]
) extends URLSupport {
  private[scala] val pathParams: Map[String, String] = route.map(r => extractPathParams(r.uri, request.uri)).getOrElse(Map())
}
