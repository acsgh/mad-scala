package com.acsgh.scala.mad.converter.json.jackson

import java.io.ByteArrayOutputStream

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.convertions.{BodyReader, BodyWriter}
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.exception.BadRequestException
import com.acsgh.scala.mad.router.http.model.Response
import com.fasterxml.jackson.databind.ObjectMapper

trait JacksonDirectives {
  directives: Directives =>

  protected def reader[T](clazz: Class[T])(implicit objectMapper: ObjectMapper): BodyReader[T] = new BodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte]): T = {
      try {
        objectMapper.readValue(body, clazz)
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T]()(implicit objectMapper: ObjectMapper): BodyWriter[T] = new BodyWriter[T] {

    override val contentType: String = "application/json; charset=UTF-8"

    override def write(body: T): Array[Byte] = {
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
