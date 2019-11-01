package acsgh.mad.scala.router.http.convertions

import acsgh.mad.scala.router.http.model.{RequestContext, Response, ResponseBuilder}

import scala.language.implicitConversions

trait DefaultFormats {

  implicit def builderToResponse(response: ResponseBuilder): Response = response.build

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String)(implicit context: RequestContext): Array[Byte] = body.getBytes("UTF-8")
  }

}
