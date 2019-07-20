package com.acsgh.scala.mad.support.swagger.dsl.model

case class Server
(
  url: String,
  description: String = null,
  variables: ServerVariables = null,
  extensions: Map[String, AnyRef] = null
)

case class ServerVariables
(

  values: Map[String, ServerVariable] = null,
  extensions: Map[String, AnyRef] = null
)

case class ServerVariable
(
  enum: Set[String] = null,
  default: String = null,
  description: String = null,
  extensions: Map[String, AnyRef] = null
)