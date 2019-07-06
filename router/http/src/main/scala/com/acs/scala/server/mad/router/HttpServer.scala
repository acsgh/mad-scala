package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.MadServer

trait HttpServer extends MadServer with HttpRouter with Routes {

  protected val httpPort: Option[Int] = None

  protected val httpRouter: HttpRouter = this

}
