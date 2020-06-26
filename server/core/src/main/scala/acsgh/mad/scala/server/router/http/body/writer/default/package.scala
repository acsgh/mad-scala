package acsgh.mad.scala.server.router.http.body.writer

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

package object default {

  implicit object DefaultHtmlBodyWriter extends HttpBodyWriter[String] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(body: String)(implicit context: HttpRequestContext): Array[Byte] = body.getBytes("UTF-8")
  }

}
