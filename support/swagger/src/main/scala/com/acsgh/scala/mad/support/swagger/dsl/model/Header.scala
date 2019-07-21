package com.acsgh.scala.mad.support.swagger.dsl.model

import enumeratum._

object Header {

  sealed trait Style extends EnumEntry

  object Style extends Enum[Style] {
    val values = findValues

    case object SIMPLE extends Style

  }

}

case class Header
(
  description: String = null,
  ref: String = null,
  required: java.lang.Boolean = null,
  deprecated: java.lang.Boolean = null,
  style: Header.Style = null,
  explode: java.lang.Boolean = null,
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  externalValue: String = null,
  content: Content = null,
  extensions: Map[String, AnyRef] = null
)
