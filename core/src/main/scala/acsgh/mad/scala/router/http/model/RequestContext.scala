package acsgh.mad.scala.router.http.model

import acsgh.mad.scala.URLSupport
import acsgh.mad.scala.router.http.HttpRouter

case class RequestContext
(
  request: Request,
  response: ResponseBuilder,
  router: HttpRouter,
  route: Option[Route[_]] = None
) extends URLSupport {
  private[scala] def ofRoute(newRoute: Route[_]): RequestContext = copy(route = Some(newRoute))

  private[scala] val pathParams: Map[String, String] = route.map(r => extractPathParams(r.uri, request.uri)).getOrElse(Map())
}
