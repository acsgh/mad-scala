---
title: 'Getting stated'
---

# Getting Started

In order to start add into your sbt dependencies the followings:

```
libraryDependencies += "com.github.acsgh.mad.scala.server.provider" %% "jetty" % "{{ site.current_version }}"
``` 

The initial hello word examples goes as:

``` scala
import acsgh.mad.scala.server.provider.jetty.JettyServerBuilder
import acsgh.mad.scala.server.{Controller, ServerBuilder}
import acsgh.mad.scala.server.router.http.body.writer.default._

object HelloWorld extends Controller with App {
  val builder: ServerBuilder = new JettyServerBuilder

  get("/") { implicit ctx =>
    responseBody("Hello world!")
  }

  builder.build().start()
}
```

``` scala
import acsgh.mad.scala.server.provider.jetty.JettyServerApp
import acsgh.mad.scala.server.router.http.listener.LoggingEventListener
import acsgh.mad.scala.server.router.ws.listener.WSLoggingEventListener
import acsgh.mad.scala.server.{Controller, Server, ServerBuilder}
import acsgh.mad.scala.server.router.http.body.writer.default._

object HelloWorld extends JettyServerApp {

  override val name: String = "Hello World Example"

  override protected def buildServer(builder: ServerBuilder): Server = {
    builder.http.addRequestListeners(LoggingEventListener)
    builder.ws.addRequestListeners(WSLoggingEventListener)

    HelloWorldRoutes(builder)

    builder.build()
  }
}

case class HelloWorldRoutes(builder: ServerBuilder) extends Controller {
  get("/") { implicit ctx =>
    responseBody("Hello world!")
  }
}
``` 

If you go to [http://localhost:6080/](http://localhost:6080/) you will see the server running

You have more completed examples in:
[here](https://github.com/acsgh/mad-scala/tree/master/examples/server)