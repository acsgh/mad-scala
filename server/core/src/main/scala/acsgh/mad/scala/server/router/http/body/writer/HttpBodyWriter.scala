package acsgh.mad.scala.server.router.http.body.writer

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpBodyWriter[T] {
  val contentType: String = "text/html"

  def write(body: T)(implicit context: HttpRequestContext): Array[Byte]
}
