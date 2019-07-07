package com.acsgh.scala.mad.converter.json.jackson

import com.acsgh.scala.mad.router.http.HttpServer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

trait JacksonHttpServer extends JacksonDirectives {
  server: HttpServer =>

  protected implicit val objectMapper: ObjectMapper = new ObjectMapper()

  onConfigure {
    configureObjectMapper()
  }


  protected def configureObjectMapper(): Unit = {
    objectMapper.registerModule(DefaultScalaModule)
  }
}
