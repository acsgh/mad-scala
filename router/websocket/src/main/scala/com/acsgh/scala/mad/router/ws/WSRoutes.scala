package com.acsgh.scala.mad.router.ws

import com.acsgh.scala.mad.router.http.convertions.{DefaultFormats, DefaultParamHandling}
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.ws.model.WSResponse

trait WSRoutes extends DefaultFormats with DefaultParamHandling with Directives {

  def ws(uri: String, subprotocol: Option[String] = None)(action: WSRequestContext => Option[WSResponse])(implicit wsRouter: WSRouter): Unit = wsRouter.route(
    WSRoute(uri, subprotocol))((context: WSRequestContext) => action(context))

}
