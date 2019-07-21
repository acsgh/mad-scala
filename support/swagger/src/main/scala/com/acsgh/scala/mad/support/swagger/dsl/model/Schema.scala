package com.acsgh.scala.mad.support.swagger.dsl.model

trait Schema[T] {
  val default: T
  val name: String
  val title: String
  val multipleOf: BigDecimal
  val maximum: BigDecimal
  val exclusiveMaximum: java.lang.Boolean
  val minimum: BigDecimal
  val exclusiveMinimum: java.lang.Boolean
  val maxLength: Integer
  val minLength: Integer
  val pattern: String
  val maxItems: Integer
  val minItems: Integer
  val uniqueItems: java.lang.Boolean
  val maxProperties: Integer
  val minProperties: Integer
  val required: List[String]
  val `type`: String
  val not: Schema[_]
  val properties: Map[String, Schema[_]]
  val additionalProperties: Object
  val description: String
  val format: String
  val $ref: String
  val nullable: java.lang.Boolean
  val readOnly: java.lang.Boolean
  val writeOnly: java.lang.Boolean
  val example: T
  val externalDocs: ExternalDocumentation
  val deprecated: java.lang.Boolean
  val xml: XML
  val extensions: Map[String, AnyRef]
  val enum: List[T]
  val discriminator: Discriminator
}

case class StringSchema
(
  default: String = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  format: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: String = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[String] = null,
  discriminator: Discriminator = null,
) extends Schema[String] {
  val `type`: String = "string"
}

case class BooleanSchema
(
  default: String = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  format: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: String = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[String] = null,
  discriminator: Discriminator = null,
) extends Schema[String] {
  val `type`: String = "java.lang.Boolean"
}

case class ArraySchema
(
  items: Schema[_] = null,
  default: String = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  format: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: String = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[String] = null,
  discriminator: Discriminator = null,
) extends Schema[String] {
  val `type`: String = "array"
}

case class ComposedSchema
(
  allOf: List[Schema[_]] = null,
  anyOf: List[Schema[_]] = null,
  oneOf: List[Schema[_]] = null,
  default: String = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  `type`: String = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  format: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: String = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[String] = null,
  discriminator: Discriminator = null,
) extends Schema[AnyRef]

case class BinarySchema
(
  default: Array[Byte] = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: Array[Byte] = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[Array[Byte]] = null,
  discriminator: Discriminator = null,
) extends Schema[Array[Byte]] {
  val `type`: String = "string"
  val format: String = "binary"
}

case class ByteArraySchema
(
  default: Array[Byte] = null,
  name: String = null,
  title: String = null,
  multipleOf: BigDecimal = null,
  maximum: BigDecimal = null,
  exclusiveMaximum: java.lang.Boolean = null,
  minimum: BigDecimal = null,
  exclusiveMinimum: java.lang.Boolean = null,
  maxLength: Integer = null,
  minLength: Integer = null,
  pattern: String = null,
  maxItems: Integer = null,
  minItems: Integer = null,
  uniqueItems: java.lang.Boolean = null,
  maxProperties: Integer = null,
  minProperties: Integer = null,
  required: List[String] = null,
  not: Schema[_] = null,
  properties: Map[String, Schema[_]] = null,
  additionalProperties: Object = null,
  description: String = null,
  $ref: String = null,
  nullable: java.lang.Boolean = null,
  readOnly: java.lang.Boolean = null,
  writeOnly: java.lang.Boolean = null,
  example: Array[Byte] = null,
  externalDocs: ExternalDocumentation = null,
  deprecated: java.lang.Boolean = null,
  xml: XML = null,
  extensions: Map[String, AnyRef] = null,
  enum: List[Array[Byte]] = null,
  discriminator: Discriminator = null,
) extends Schema[Array[Byte]] {
  val `type`: String = "string"
  val format: String = "byte"
}

case class Discriminator
(
  propertyName: String,
  mapping: Map[String, String]
)

case class XML
(
  name: String = null,
  namespace: String = null,
  prefix: String = null,
  attribute: java.lang.Boolean = null,
  wrapped: java.lang.Boolean = null,
  extensions: Map[String, AnyRef] = null
)