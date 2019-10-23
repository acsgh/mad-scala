package acsgh.mad.scala.converter.json.jackson

import acsgh.mad.scala.router.http.HttpServer
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
