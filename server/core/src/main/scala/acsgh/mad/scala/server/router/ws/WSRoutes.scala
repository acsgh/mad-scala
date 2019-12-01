package acsgh.mad.scala.server.router.ws

import acsgh.mad.scala.server.router.ws.directives.WSDirectives
import acsgh.mad.scala.server.router.ws.model.WSRouteAction

trait WSRoutes extends WSDirectives {

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRouteAction): Unit = route(uri, subprotocols)(action)

  protected def route(uri: String, subprotocols: Set[String] = Set())(handler: WSRouteAction): Unit
}
