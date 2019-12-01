package acsgh.mad.scala.server.router.ws.model

case class WSRoute
(
  uri: String,
  subprotocols: Set[String],
  handler: WSRouteAction
)
