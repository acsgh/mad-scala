package acsgh.mad.scala.router.ws

package object model {
  type WSRouteAction = WSRequestContext => Option[WSResponse]
}
