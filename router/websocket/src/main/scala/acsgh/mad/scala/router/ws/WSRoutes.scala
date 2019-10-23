package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.http.convertions.{DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.ws.model.WSResponse

trait WSRoutes extends DefaultFormats with DefaultParamHandling with Directives {

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRequestContext => Option[WSResponse])(implicit wsRouter: WSRouter): Unit = wsRouter.route(uri, subprotocols)((context: WSRequestContext) => action(context))
}
