package acsgh.mad.scala.router.http.convertions

import acsgh.mad.scala.router.http.model.{Response, ResponseBuilder, Route, RouteError}

import scala.language.implicitConversions

trait DefaultFormats {

  implicit def builderToResponse(builder: ResponseBuilder): Response = builder.build

  implicit def responseToSuccess(response: ResponseBuilder): Either[RouteError, Response] = Right(response)

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String): Array[Byte] = body.getBytes("UTF-8")
  }

}
