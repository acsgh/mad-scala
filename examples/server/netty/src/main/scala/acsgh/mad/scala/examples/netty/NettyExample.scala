package acsgh.mad.scala.examples.netty

import acsgh.mad.scala.server.converter.json.spray.{JsonErrorCodeHandler, JsonExceptionHandler}
import acsgh.mad.scala.server.provider.netty.NettyServerApp
import acsgh.mad.scala.server.router.http.listener.LoggingEventListener
import acsgh.mad.scala.server.router.ws.listener.WSLoggingEventListener
import acsgh.mad.scala.server.{Server, ServerBuilder}

object NettyExample extends NettyServerApp {

  override val name: String = "Netty Boot Example"

  override protected def buildServer(builder: ServerBuilder): Server = {
    builder.http.addRequestListeners(LoggingEventListener)
    builder.ws.addRequestListeners(WSLoggingEventListener)

    builder.http.resourceFolder("/", "public")
    builder.http.webjars()
    builder.http.defaultErrorCodeHandler(new JsonErrorCodeHandler())
    builder.http.exceptionHandler(new JsonExceptionHandler())
    PersonRoutes(builder)

    builder.build()
  }
}
