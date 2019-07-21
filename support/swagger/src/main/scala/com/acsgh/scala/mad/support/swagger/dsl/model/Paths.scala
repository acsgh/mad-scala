package com.acsgh.scala.mad.support.swagger.dsl.model

import io.swagger.v3.oas.models

case class Paths
(
  values: Map[String, PathItem],
  extensions: Map[String, AnyRef] = null
)

case class PathItem
(
  summary: String = null,
  description: String = null,
  get: models.Operation = null,
  put: Operation = null,
  post: Operation = null,
  delete: Operation = null,
  options: Operation = null,
  head: Operation = null,
  patch: Operation = null,
  trace: Operation = null,
  servers: List[Server] = null,
  parameters: List[Parameter] = null,
  ref: String = null,
  extensions: Map[String, AnyRef] = null
)


case class Operation
(
  tags: List[Tag] = null,
  summary: String = null,
  description: String = null,
  externalDocs: ExternalDocumentation = null,
  operationId: String = null,
  parameters: List[Parameter] = null,
  requestBody: RequestBody = null,
  responses: ApiResponses = null,
  callbacks: Map[String, Callback] = null,
  deprecated: java.lang.Boolean = null,
  security: List[SecurityRequirement] = null,
  servers: List[Server] = null,
  extensions: Map[String, AnyRef] = null
)