package com.acsgh.scala.mad.support.swagger.dsl.model

import com.acsgh.scala.mad.router.http.model.RequestMethod

case class OpenAPI
(
  openapi: String = "3.0.1",
  info: Info = null,
  externalDocs: ExternalDocumentation = null,
  servers: List[Server] = null,
  security: List[SecurityRequirement] = null,
  tags: List[Tag] = null,
  extensions: Map[String, AnyRef] = null
) {

  def operation(uri: String, method: RequestMethod, operation: Operation): OpenAPI = {
    this
  }
}
