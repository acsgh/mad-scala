package acsgh.mad.scala.server.router.ws

import acsgh.mad.scala.core.ws.model.WSResponse

package object model {
  type WSRouteAction = WSRequestContext => Option[WSResponse]
}
