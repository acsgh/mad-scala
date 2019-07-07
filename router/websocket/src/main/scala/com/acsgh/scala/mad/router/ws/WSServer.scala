package com.acsgh.scala.mad.router.ws

import com.acsgh.scala.mad.router.http.HttpServer
import com.acsgh.scala.mad.router.ws.directives.Directives

trait WSServer extends HttpServer with WSRouter with WSRoutes with Directives {
  protected implicit val wsRouter: WSRouter = this
}
