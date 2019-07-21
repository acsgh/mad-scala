package com.acsgh.scala.mad.support.swagger.dsl.model

case class Callback
(
  values: Map[String, PathItem],
  ref: String = null,
  extensions: Map[String, AnyRef] = null
)