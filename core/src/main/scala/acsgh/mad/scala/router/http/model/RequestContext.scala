package acsgh.mad.scala.router.http.model

import acsgh.mad.scala.URLSupport
import acsgh.mad.scala.router.http.HttpRouter

case class RequestContext
(
  request: Request,
  response: ResponseBuilder,
  router: HttpRouter,
  route: Option[Route[RouteAction]]
) extends URLSupport {
  private[scala] val pathParams: Map[String, String] = route.map(r => extractPathParams(r.uri, request.uri)).getOrElse(Map())
}
