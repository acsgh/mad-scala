package com.acs.scala.server.mad.converter.json.jackson

import java.io.ByteArrayOutputStream

import com.acs.scala.server.mad.router.http.RequestContext
import com.acs.scala.server.mad.router.http.convertions.{BodyReader, BodyWriter}
import com.acs.scala.server.mad.router.http.directives.Directives
import com.acs.scala.server.mad.router.http.exception.BadRequestException
import com.acs.scala.server.mad.router.http.model.Response

trait JacksonDirectives {
  directives: Directives =>

  protected val jacksonHttpServer: JacksonHttpServer

  protected def reader[T](clazz: Class[T]): BodyReader[T] = new BodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte]): T = {
      try {
        jacksonHttpServer.objectMapper.readValue(body, clazz)
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T](): BodyWriter[T] = new BodyWriter[T] {

    override val contentType: String = "application/json"

    override def write(body: T): Array[Byte] = {
      try {
        val out = new ByteArrayOutputStream
        jacksonHttpServer.objectMapper.writeValue(out, body)
        out.toByteArray
      } catch {
        case e: Exception => throw new RuntimeException(e)
      }
    }
  }

  def requestJson[T](clazz: Class[T])(action: T => Response)(implicit context: RequestContext): Response = requestBody(action)(context, reader(clazz))

  def responseJson[T](value: T)(implicit context: RequestContext): Response = responseBody(value)(context, writer())

}
