package com.acsgh.scala.mad.support.swagger.dsl.model

import enumeratum._

case class MediaType
(
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  example: AnyRef = null,
  encoding: Map[String, Encoding],
  extensions: Map[String, AnyRef] = null
)


object Encoding {

  sealed trait Style extends EnumEntry

  object Style extends Enum[Style] {
    val values = findValues

    case object FORM extends Style

    case object SPACE_DELIMITED extends Style

    case object PIPE_DELIMITED extends Style

    case object DEEP_OBJECT extends Style

  }

}

case class Encoding
(
  contentType: String = null,
  headers: Map[String, Header] = null,
  style: Encoding.Style = null,
  explode: java.lang.Boolean = null,
  allowReserved: java.lang.Boolean = null,
  extensions: Map[String, AnyRef] = null
)
