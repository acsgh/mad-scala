package acsgh.mad.scala.server.router.http.convertions

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait HttpParamReader[I, O] {
  def read(input: I): O
}

trait HttpParamWriter[I, O] {
  def write(input: I): O
}