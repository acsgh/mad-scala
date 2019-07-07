package com.acs.scala.server.mad.router.http.convertions

import com.acs.scala.server.mad.router.http.model.{Response, ResponseBuilder}

trait DefaultFormats {

  implicit def builderToResponse(builder: ResponseBuilder): Response = builder.build

  implicit object HtmlBodyWriter extends BodyWriter[String] {
    override val contentType: String = "text/html"

    override def write(body: String): Array[Byte] = body.getBytes("UTF-8")
  }

}