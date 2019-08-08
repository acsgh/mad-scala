package com.acsgh.scala.mad.converter.json.jackson

import com.acsgh.scala.mad.router.http.HttpServer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait JacksonHttpServer extends JacksonDirectives {
  server: HttpServer =>

  protected implicit val objectMapper: ObjectMapper = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}
