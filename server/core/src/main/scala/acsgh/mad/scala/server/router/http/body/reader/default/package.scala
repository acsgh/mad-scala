package acsgh.mad.scala.server.router.http.body.reader

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

package object default {

  implicit object HtmlBodyReader extends HttpBodyReader[String] {

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): String = new String(body, "UTF-8")
  }

}
