package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.http.convertions.DefaultParamHandling
import acsgh.mad.scala.router.ws.convertions.DefaultFormats
import acsgh.mad.scala.router.ws.directives.Directives
import acsgh.mad.scala.router.ws.model.WSResponse

trait WSRoutes extends DefaultFormats with DefaultParamHandling with Directives {

  protected val wsRouter: WSRouter

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRequestContext => Option[WSResponse]): Unit = wsRouter.route(uri, subprotocols)((context: WSRequestContext) => action(context))
}
