package com.acsgh.scala.mad.support.swagger.dsl.model

case class ApiResponse
(
  description: String = null,
  headers: Map[String, Header] = null,
  content: Content = null,
  ref: String = null,
  links: Map[String, Link] = null,
  extensions: Map[String, AnyRef] = null
)
