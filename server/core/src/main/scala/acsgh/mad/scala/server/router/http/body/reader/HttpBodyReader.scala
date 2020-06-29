package acsgh.mad.scala.server.router.http.body.reader

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpBodyReader[T] {
  val contentTypes: Set[String] = Set()

  val strictContentTypes: Boolean = false

  def read(body: Array[Byte])(implicit context: HttpRequestContext): T
}