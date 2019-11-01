package acsgh.mad.scala.router.http.model

import acsgh.mad.scala.URLSupport
import acsgh.mad.scala.router.http.HttpRouter

case class RequestContext
(
  request: Request,
  response: ResponseBuilder,
  private[scala] val router: HttpRouter,
  route: Option[Route[_]] = None
) extends URLSupport {
  def ofRoute(newRoute: Route[_]): RequestContext = copy(route = Some(newRoute))

  val pathParams: Map[String, String] = route.map(r => extractPathParams(r.uri, request.uri)).getOrElse(Map())
}
