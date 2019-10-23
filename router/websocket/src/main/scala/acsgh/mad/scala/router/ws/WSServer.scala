package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.http.HttpServer
import acsgh.mad.scala.router.ws.directives.Directives

trait WSServer extends HttpServer with WSRouter with WSRoutes with Directives {
  protected implicit val wsRouter: WSRouter = this
}
