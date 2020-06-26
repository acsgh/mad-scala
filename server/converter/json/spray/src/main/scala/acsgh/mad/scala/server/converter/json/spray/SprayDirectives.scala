package acsgh.mad.scala.server.converter.json.spray

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.server.router.http.body.reader._
import acsgh.mad.scala.server.router.http.body.writer._
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import spray.json._

trait SprayDirectives {
  directives: HttpDirectives =>

  protected def reader[T](clazz: Class[T])(implicit jsonReader: JsonReader[T]): HttpBodyReader[T] = new HttpBodyReader[T] {
    override val contentTypes: Set[String] = Set("application/json")

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): T = {
      try {
        new String(body, "UTF-8").parseJson.convertTo[T]
      } catch {
        case e: Exception => throw new BadRequestException(e)
      }
    }
  }

  protected def writer[T]()(implicit jsonWriter: JsonWriter[T]): HttpBodyWriter[T] = new HttpBodyWriter[T] {

    override val contentType: String = "application/json; charset=UTF-8"

    override def write(body: T)(implicit context: HttpRequestContext): Array[Byte] = {
      try {
        val json = if (context.router.productionMode) {
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

  def requestJson[T](clazz: Class[T])(action: T => HttpResponse)(implicit context: HttpRequestContext, jsonReader: JsonReader[T]): HttpResponse = requestBody(action)(context, reader(clazz))

  def responseJson[T](value: T)(implicit context: HttpRequestContext, jsonWriter: JsonWriter[T]): HttpResponse = responseBody(value)(context, writer())

}
