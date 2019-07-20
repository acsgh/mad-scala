package com.acsgh.scala.mad.support.swagger.dsl.converter

trait Converter[I >: Null, O >: Null] {

  val input: I

  final def asJava: O = if (input != null) asJavaNullSafe else null

  def asJavaNullSafe: O
}
