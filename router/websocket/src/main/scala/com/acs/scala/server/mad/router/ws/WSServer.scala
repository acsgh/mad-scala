package com.acs.scala.server.mad.router.ws

import com.acs.scala.server.mad.router.http.HttpServer
import com.acs.scala.server.mad.router.ws.directives.Directives

trait WSServer extends HttpServer with WSRouter with Directives {

}
