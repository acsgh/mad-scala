package com.acsgh.scala.mad.support.swagger.dsl.model

case class ExternalDocumentation
(
  description: String = null,
  url: String = null,
  extensions: Map[String, AnyRef] = null
)

case class Tag
(
  name: String,
  description: String = null,
  externalDocs: ExternalDocumentation = null,
  extensions: Map[String, AnyRef] = null
)