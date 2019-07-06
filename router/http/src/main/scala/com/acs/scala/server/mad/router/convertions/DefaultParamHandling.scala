package com.acs.scala.server.mad.router.convertions

trait DefaultParamHandling {

  implicit object StringWriter extends ParamWriter[String] {
    override def write(input: String): String = input
  }

  implicit object StringReader extends ParamReader[String] {
    override def read(input: String): String = input
  }

}
