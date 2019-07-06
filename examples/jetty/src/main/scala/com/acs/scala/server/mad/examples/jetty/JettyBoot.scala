package com.acs.scala.server.mad.examples.jetty

import com.acs.scala.server.mad.provider.jetty.JettyMadServer

object JettyBoot extends JettyMadServer{
  override val name: String = "Jetty Boot Example"

  override protected val httpPort: Option[Int] = Some(7654)
}
