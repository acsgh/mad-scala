package acsgh.mad.scala.server.router.http.convertions

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpParamReader[T] {
  def read(input: String): T
}

trait HttpParamWriter[T] {
  def write(input: T): String
}

trait HttpBodyReader[T] {
  val contentTypes: Set[String] = Set()

  val strictContentTypes: Boolean = false

  def read(body: Array[Byte])(implicit context: HttpRequestContext): T
}

trait HttpBodyWriter[T] {
  val contentType: String = "text/html"

  def write(body: T)(implicit context: HttpRequestContext): Array[Byte]
}