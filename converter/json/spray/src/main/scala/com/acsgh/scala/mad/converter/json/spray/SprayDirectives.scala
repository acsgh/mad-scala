package com.acsgh.scala.mad.converter.json.spray

import com.acsgh.scala.mad.ProductionInfo
import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.convertions.{BodyReader, BodyWriter}
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.exception.BadRequestException
import com.acsgh.scala.mad.router.http.model.Response
import spray.json._

trait SprayDirectives extends ProductionInfo {
  directives: Directives =>

  protected def reader[T](clazz: Class[T])(implicit jsonReader: JsonReader[T]): BodyReader[T] = new BodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte]): T = {
      try {
        new String(body, "UTF_8").parseJson.convertTo[T]
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T]()(implicit jsonWriter: JsonWriter[T]): BodyWriter[T] = new BodyWriter[T] {

    override val contentType: String = "application/json; charset=UTF-8"

    override def write(body: T): Array[Byte] = {
      try {
        val json = if (productionMode) {
          body.toJson.compactPrint
        } else {
          body.toJson.prettyPrint
        }
        json.getBytes("UTF-8")
      } catch {
        case e: Exception => throw new RuntimeException(e)
      }
    }
  }

  def requestJson[T](clazz: Class[T])(action: T => Response)(implicit context: RequestContext, jsonReader: JsonReader[T]): Response = requestBody(action)(context, reader(clazz))

  def responseJson[T](value: T)(implicit context: RequestContext, jsonWriter: JsonWriter[T]): Response = responseBody(value)(context, writer())

}
