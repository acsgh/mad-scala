package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.ws.directives.Directives
import acsgh.mad.scala.router.ws.model.WSRouteAction

trait WSRoutes extends Directives {

  protected val wsRouter: WSRouter

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRouteAction): Unit = wsRouter.route(uri, subprotocols)(action)
}
