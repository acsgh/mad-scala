package acsgh.mad.scala.router.ws.model

case class WSRoute
(
  uri: String,
  subprotocols: Set[String],
  handler: WSRouteAction
)
