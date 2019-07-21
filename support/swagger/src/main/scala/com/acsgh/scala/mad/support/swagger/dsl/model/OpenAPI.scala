package com.acsgh.scala.mad.support.swagger.dsl.model

import com.acsgh.scala.mad.router.http.model.RequestMethod
import io.swagger.v3.oas.models

case class OpenAPI
(
  openapi: String = "3.0.1",
  info: Info = null,
  externalDocs: ExternalDocumentation = null,
  servers: List[Server] = null,
  security: List[SecurityRequirement] = null,
  tags: List[Tag] = null,
  paths:Paths = null,
  components: Components = null,
  extensions: Map[String, AnyRef] = null
) {

  def operation(uri: String, method: RequestMethod, operation: Operation): OpenAPI = {
    this
  }
}

case class Components
(
  schemas: Map[String, Schema[_]] = null,
  responses: Map[String, ApiResponse] = null,
  parameters: Map[String, Parameter] = null,
  examples: Map[String, Example] = null,
  requestBodies: Map[String, RequestBody] = null,
  headers: Map[String, Header] = null,
  securitySchemes: Map[String, SecurityScheme] = null,
  links: Map[String, Link] = null,
  callbacks: Map[String, Callback] = null,
  extensions: Map[String, AnyRef] = null
)