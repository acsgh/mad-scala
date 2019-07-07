package com.acs.scala.server.mad.converter.json.jackson

import com.acs.scala.server.mad.router.HttpServer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

trait JacksonHttpServer extends JacksonDirectives {
  server: HttpServer =>

  private[jackson] val objectMapper: ObjectMapper = new ObjectMapper()

  onConfigure {
    configureObjectMapper()
  }


  override protected val jacksonHttpServer: JacksonHttpServer = this

  protected def configureObjectMapper(): Unit = {
    objectMapper.registerModule(DefaultScalaModule)
  }
}
