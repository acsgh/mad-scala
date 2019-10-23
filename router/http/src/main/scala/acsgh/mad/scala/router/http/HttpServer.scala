package acsgh.mad.scala.router.http

import acsgh.mad.scala.MadServer

trait HttpServer extends MadServer with HttpRouter with Routes {

  protected val httpPort: Option[Int] = None

  protected implicit val httpRouter: HttpRouter = this

}
