package com.acsgh.scala.mad.support.swagger.dsl.model

case class Info
(
  title: String = null,
  description: String = null,
  termsOfService: String = null,
  contact: Contact = null,
  license: License = null,
  version: String = null,
  extensions: Map[String, AnyRef] = null
)

case class Contact
(
  name: String = null,
  url: String = null,
  email: String = null,
  extensions: Map[String, AnyRef] = null
)

case class License
(
  name: String = null,
  url: String = null,
  extensions: Map[String, AnyRef] = null
)