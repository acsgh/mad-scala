package com.acs.scala.server.mad.examples.jetty

import com.acs.scala.server.mad.provider.jetty.JettyMadServer
import com.acs.scala.server.mad.router.directives.Directives

object JettyBoot extends JettyMadServer with Directives {
  override val name: String = "Jetty Boot Example"

  override protected val httpPort: Option[Int] = Some(7654)

  get("/hello") { implicit (request, response) =>
    header("Content-Type") { contentType =>
      response.body("Hello").build
    }
  }
}
