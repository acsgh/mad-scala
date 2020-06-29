package acsgh.mad.scala.server.converter.json.jackson

import java.io.ByteArrayOutputStream

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.server.router.http.body.reader._
import acsgh.mad.scala.server.router.http.body.writer._
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import com.fasterxml.jackson.databind.ObjectMapper

trait JacksonDirectives {
  directives: HttpDirectives =>

  protected def reader[T](clazz: Class[T])(implicit objectMapper: ObjectMapper): HttpBodyReader[T] = new HttpBodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): T = {
      try {
        objectMapper.readValue(body, clazz)
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T]()(implicit objectMapper: ObjectMapper): HttpBodyWriter[T] = new HttpBodyWriter[T] {

    override val contentType: String = "application/json; charset=UTF-8"

    override def write(body: T)(implicit context: HttpRequestContext): Array[Byte] = {
      try {
        val out = new ByteArrayOutputStream
        objectMapper.writeValue(out, body)
        out.toByteArray
      } catch {
        case e: Exception => throw new RuntimeException(e)
      }
    }
  }

  def requestJson[T](clazz: Class[T])(action: T => HttpResponse)(implicit context: HttpRequestContext, objectMapper: ObjectMapper): HttpResponse = requestBody(action)(context, reader(clazz))

  def responseJson[T](value: T)(implicit context: HttpRequestContext, objectMapper: ObjectMapper): HttpResponse = responseBody(value)(context, writer())

}
