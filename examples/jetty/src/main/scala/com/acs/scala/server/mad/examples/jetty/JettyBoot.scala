package com.acs.scala.server.mad.examples.jetty

import com.acs.scala.server.mad.provider.jetty.JettyMadServer
import com.acs.scala.server.mad.router.directives.Directives

object JettyBoot extends JettyMadServer with Directives {
  override val name: String = "Jetty Boot Example"

  override protected val httpPort: Option[Int] = Some(7654)

  get("/hello") { implicit context =>
    queryParam("name") { name =>
      responseHeader("Location", "asd") {
        context.response.body(s"Hello: $name")
      }
    }
  }

  get("/hi") { implicit context =>
    redirect("/hello?name=alberto")
  }
}
