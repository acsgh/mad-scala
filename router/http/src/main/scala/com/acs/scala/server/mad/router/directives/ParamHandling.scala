package com.acs.scala.server.mad.router.directives


trait ParamReader[T] {
  def read(input: String): T
}

trait ParamWriter[T] {
  def write(input: T): String
}
