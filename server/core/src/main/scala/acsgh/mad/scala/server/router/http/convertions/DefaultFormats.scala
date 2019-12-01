package acsgh.mad.scala.server.router.http.convertions

import acsgh.mad.scala.core.http.model.{HttpResponse, HttpResponseBuilder}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

import scala.language.implicitConversions

trait DefaultFormats {

  implicit def builderToResponse(response: HttpResponseBuilder): HttpResponse = response.build

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String)(implicit context: HttpRequestContext): Array[Byte] = body.getBytes("UTF-8")
  }

}
