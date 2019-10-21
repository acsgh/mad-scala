package com.acsgh.scala.mad.router.http.convertions

import com.acsgh.scala.mad.router.http.model.{Response, ResponseBuilder}

trait DefaultFormats {

  implicit def builderToResponse(builder: ResponseBuilder): Response = builder.build

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String): Array[Byte] = body.getBytes("UTF-8")
  }

}
