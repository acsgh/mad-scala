package acsgh.mad.scala.server.provider.netty

import acsgh.mad.scala.server.{ServerApp, ServerBuilder}

trait NettyServerApp extends ServerApp {

  override protected def newServerBuilder(): ServerBuilder = new NettyServerBuilder()
}
