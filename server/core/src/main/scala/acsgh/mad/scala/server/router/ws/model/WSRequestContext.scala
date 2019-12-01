package acsgh.mad.scala.server.router.ws.model

import acsgh.mad.scala.core.ws.model.WSRequest

case class WSRequestContext
(
  request: WSRequest,
  route: Option[WSRoute] = None
)
