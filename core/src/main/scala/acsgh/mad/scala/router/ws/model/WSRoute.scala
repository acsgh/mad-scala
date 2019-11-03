package acsgh.mad.scala.router.ws.model

case class WSRoute
(
  subprotocols: Set[String],
  handler: WSRouteAction
)
