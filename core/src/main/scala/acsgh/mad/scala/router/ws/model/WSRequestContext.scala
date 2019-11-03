package acsgh.mad.scala.router.ws.model

case class WSRequestContext
(
  request: WSRequest,
  route: Option[WSRoute] = None
)
