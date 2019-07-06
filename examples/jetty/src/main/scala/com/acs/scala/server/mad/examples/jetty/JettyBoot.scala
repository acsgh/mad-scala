package com.acs.scala.server.mad.examples.jetty

import com.acs.scala.server.mad.provider.jetty.JettyMadServer
import com.acs.scala.server.mad.router.directives.Directives

object JettyBoot extends JettyMadServer with Directives {
  override val name: String = "Jetty Boot Example"

  override protected val httpPort: Option[Int] = Some(7654)

  get("/hello") { implicit context =>
    requestQuery("name".list, "age".as[Long]) { (name, age) =>
      requestHeader("Host") { host =>
        responseHeader("Location", "asd") {
          responseBody(s"Hello: $name you have $age ---> $host")
        }
      }
    }
  }

  get("/hi") { implicit context =>
    redirect("/hello?name=alberto")
  }
}
