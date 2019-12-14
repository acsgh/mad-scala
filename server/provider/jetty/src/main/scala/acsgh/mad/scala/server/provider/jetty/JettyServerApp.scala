package acsgh.mad.scala.server.provider.jetty

import acsgh.mad.scala.server.{ServerApp, ServerBuilder}

trait JettyServerApp extends ServerApp {

  override protected def newServerBuilder(): ServerBuilder = new JettyServerBuilder()
}
