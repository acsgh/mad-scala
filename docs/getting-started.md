---
current_version: '0.8.0'
title: 'Getting stated'
---

# Getting Started

In order to start add into your sbt dependencies the followings:

```
libraryDependencies += "com.github.acsgh.mad.scala.server.provider" %% "jetty" % "{{ page.current_version}}"
libraryDependencies += "com.github.acsgh.mad.scala.server.provider" %% "jetty" % "{{ latest_release}}"
``` 

The initial hello word example goes as:

``` scala
import acsgh.mad.scala.server.provider.jetty.JettyServerApp
import acsgh.mad.scala.server.router.http.listener.LoggingEventListener
import acsgh.mad.scala.server.router.ws.listener.WSLoggingEventListener
import acsgh.mad.scala.server.{Controller, Server, ServerBuilder}

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

You have more completed examples in:
[here](https://github.com/acsgh/mad-scala/tree/master/examples/server)