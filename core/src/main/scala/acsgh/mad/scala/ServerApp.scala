package acsgh.mad.scala

import com.acsgh.common.scala.App

trait ServerApp extends App {

  protected var server: Server = _

  protected def buildServer(builder: ServerBuilder): Server

  onConfigure {
    val builder = new ServerBuilder()
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
