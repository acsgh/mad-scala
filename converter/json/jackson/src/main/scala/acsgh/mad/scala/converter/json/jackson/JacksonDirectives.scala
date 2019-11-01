package acsgh.mad.scala.converter.json.jackson

import java.io.ByteArrayOutputStream

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.convertions.{BodyReader, BodyWriter}
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.model.Response
import com.fasterxml.jackson.databind.ObjectMapper

trait JacksonDirectives {
  directives: Directives =>

  protected def reader[T](clazz: Class[T])(implicit objectMapper: ObjectMapper): BodyReader[T] = new BodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte])(implicit context: RequestContext): T = {
      try {
        objectMapper.readValue(body, clazz)
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T]()(implicit objectMapper: ObjectMapper): BodyWriter[T] = new BodyWriter[T] {

    override val contentType: String = "application/json; charset=UTF-8"

    override def write(body: T)(implicit context: RequestContext): Array[Byte] = {
      try {
        val out = new ByteArrayOutputStream
        objectMapper.writeValue(out, body)
        out.toByteArray
      } catch {
        case e: Exception => throw new RuntimeException(e)
      }
    }
  }

  def requestJson[T](clazz: Class[T])(action: T => Response)(implicit context: RequestContext, objectMapper: ObjectMapper): Response = requestBody(action)(context, reader(clazz))

  def responseJson[T](value: T)(implicit context: RequestContext, objectMapper: ObjectMapper): Response = responseBody(value)(context, writer())

}
