package acsgh.mad.scala.server

import com.acsgh.common.scala.App

trait ServerApp extends App {

  protected var server: Server = _

  protected def newServerBuilder():ServerBuilder

  protected def buildServer(builder: ServerBuilder): Server

  onConfigure {
    val builder = newServerBuilder()
    builder.name(name)
    server = buildServer(builder)
  }

  onStart {
    server.start()
  }

  onStop {
    server.stop()
  }
}
