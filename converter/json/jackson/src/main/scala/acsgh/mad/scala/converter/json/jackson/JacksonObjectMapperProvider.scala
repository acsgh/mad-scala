package acsgh.mad.scala.converter.json.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object JacksonObjectMapperProvider {

  def build(productionMode: Boolean): ObjectMapper = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper
  }
}
