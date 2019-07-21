package com.acsgh.scala.mad.support.swagger.dsl.model

import enumeratum._

object Parameter {

  sealed trait Style extends EnumEntry

  object Type extends Enum[Style] {
    val values = findValues

    case object MATRIX extends Style

    case object LABEL extends Style

    case object FORM extends Style

    case object SIMPLE extends Style

    case object SPACEDELIMITED extends Style

    case object PIPEDELIMITED extends Style

    case object DEEPOBJECT extends Style

  }

}

case class RequestBody
(
  description: String = null,
  content: Content = null,
  required: java.lang.Boolean = null,
  ref: String = null,
  extensions: Map[String, AnyRef] = null
)

trait Parameter {
  val name: String
  val in: String
  val description: String
  val required: java.lang.Boolean
  val deprecated: java.lang.Boolean
  val allowEmptyValue: java.lang.Boolean
  val ref: String
  val style: Parameter.Style
  val explode: java.lang.Boolean
  val allowReserved: java.lang.Boolean
  val schema: Schema[_]
  val examples: Map[String, Example]
  val example: Object
  val content: Content
  val extensions: Map[String, AnyRef]
}

case class CookieParameter
(
  name: String = null,
  description: String = null,
  required: java.lang.Boolean = null,
  deprecated: java.lang.Boolean = null,
  allowEmptyValue: java.lang.Boolean = null,
  ref: String = null,
  style: Parameter.Style = null,
  explode: java.lang.Boolean = null,
  allowReserved: java.lang.Boolean = null,
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  example: Object = null,
  content: Content = null,
  extensions: Map[String, AnyRef] = null
) extends Parameter {
  val in: String = "cookie"
}

case class HeaderParameter
(
  name: String = null,
  description: String = null,
  required: java.lang.Boolean = null,
  deprecated: java.lang.Boolean = null,
  allowEmptyValue: java.lang.Boolean = null,
  ref: String = null,
  style: Parameter.Style = null,
  explode: java.lang.Boolean = null,
  allowReserved: java.lang.Boolean = null,
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  example: Object = null,
  content: Content = null,
  extensions: Map[String, AnyRef] = null
) extends Parameter {
  val in: String = "header"
}

case class PathParameter
(
  name: String = null,
  description: String = null,
  deprecated: java.lang.Boolean = null,
  allowEmptyValue: java.lang.Boolean = null,
  ref: String = null,
  style: Parameter.Style = null,
  explode: java.lang.Boolean = null,
  allowReserved: java.lang.Boolean = null,
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  example: Object = null,
  content: Content = null,
  extensions: Map[String, AnyRef] = null
) extends Parameter {
  val in: String = "path"
  val required: java.lang.Boolean = true
}

case class QueryParameter
(
  name: String = null,
  description: String = null,
  required: java.lang.Boolean = null,
  deprecated: java.lang.Boolean = null,
  allowEmptyValue: java.lang.Boolean = null,
  ref: String = null,
  style: Parameter.Style = null,
  explode: java.lang.Boolean = null,
  allowReserved: java.lang.Boolean = null,
  schema: Schema[_] = null,
  examples: Map[String, Example] = null,
  example: Object = null,
  content: Content = null,
  extensions: Map[String, AnyRef] = null
) extends Parameter {
  val in: String = "query"
}