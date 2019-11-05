package acsgh.mad.scala.support.swagger

import com.acsgh.common.scala.App

trait ServerSwaggerApp extends App with ServerSwaggerDelegate {

  override protected var server: ServerSwagger = new ServerSwagger()

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
