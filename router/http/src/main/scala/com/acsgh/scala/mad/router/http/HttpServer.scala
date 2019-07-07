package com.acsgh.scala.mad.router.http

import com.acsgh.scala.mad.MadServer

trait HttpServer extends MadServer with HttpRouter with Routes {

  protected val httpPort: Option[Int] = None

  protected implicit val httpRouter: HttpRouter = this

}
