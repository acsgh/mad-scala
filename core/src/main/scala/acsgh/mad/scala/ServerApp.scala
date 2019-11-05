package acsgh.mad.scala

import com.acsgh.common.scala.App

trait ServerApp extends App with ServerDelegate[Server] {

  override protected var server: Server = new Server()

  onConfigure {
    server.name(name)
  }

  onStart {
    server.start()
  }

  onStop {
    server.stop()
  }
}
