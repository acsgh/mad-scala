package com.acsgh.scala.mad.support.swagger.dsl.model

case class Link
(
  operationRef: String = null,
  operationId: String = null,
  parameters: Map[String, String] = null,
  requestBody: AnyRef = null,
  headers: Map[String, Header] = null,
  description: String = null,
  ref: String = null,
  server: Server = null,
  extensions: Map[String, AnyRef] = null
)

case class LinkParameter
(
  value: String = null,
  extensions: Map[String, AnyRef] = null
)
