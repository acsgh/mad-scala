package acsgh.mad.scala.server.router.http.convertions

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpParamReader[T] {
  def read(input: String): T
}

trait HttpParamWriter[T] {
  def write(input: T): String
}