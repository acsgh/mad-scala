package acsgh.mad.scala.router.http.convertions

import acsgh.mad.scala.router.http.model.RequestContext

trait ParamReader[T] {
  def read(input: String): T
}

trait ParamWriter[T] {
  def write(input: T): String
}

trait BodyReader[T] {
  val contentTypes: Set[String] = Set()

  val strictContentTypes: Boolean = false

  def read(body: Array[Byte])(implicit context: RequestContext): T
}

trait BodyWriter[T] {
  val contentType: String = "text/html"

  def write(body: T)(implicit context: RequestContext): Array[Byte]
}