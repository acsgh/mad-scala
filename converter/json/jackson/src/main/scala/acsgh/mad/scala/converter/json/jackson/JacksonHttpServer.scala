package acsgh.mad.scala.converter.json.jackson

import acsgh.mad.scala.Server
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait JacksonHttpServer extends JacksonDirectives {
  server: Server =>

  protected implicit val objectMapper: ObjectMapper = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}
