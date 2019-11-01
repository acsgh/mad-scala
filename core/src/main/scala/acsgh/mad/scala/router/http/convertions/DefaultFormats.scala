package acsgh.mad.scala.router.http.convertions

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.model.{Response, ResponseBuilder, Route, RouteError, RouteResult}

import scala.language.implicitConversions

trait DefaultFormats {

//  implicit def builderToResponse(builder: ResponseBuilder): RouteResult = Right(builder.build)

  implicit def responseToSuccess(response: ResponseBuilder): RouteResult = Right(response.build)

  implicit def responseToSuccess2(response: Response): RouteResult = Right(response)

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String)(implicit context: RequestContext): Array[Byte] = body.getBytes("UTF-8")
  }

}
