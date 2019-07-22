package com.acsgh.scala.mad.support.swagger.dsl

import io.swagger.v3.oas.models._
import io.swagger.v3.oas.models.callbacks.Callback
import io.swagger.v3.oas.models.info._
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.{ApiResponse, ApiResponses}
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.servers.{Server, ServerVariable, ServerVariables}
import io.swagger.v3.oas.models.tags.Tag

import scala.collection.JavaConverters._

trait OpenApiBuilder {

  def OpenAPI
  (
    openapi: String = "3.0.1",
    info: Info = null,
    externalDocs: ExternalDocumentation = null,
    servers: List[Server] = null,
    security: List[SecurityRequirement] = null,
    tags: List[Tag] = null,
    paths: Paths = null,
    components: Components = null,
    extensions: Map[String, AnyRef] = null
  ): OpenAPI = {
    val result = new OpenAPI()
    result.openapi(openapi)
    result.info(info)
    result.externalDocs(externalDocs)
    result.servers(servers.asJava)
    result.security(security.asJava)
    result.tags(tags.asJava)
    result.paths(paths)
    result.components(components)
    result.extensions(extensions.asJava)
    result
  }

  def Info
  (
    title: String = null,
    description: String = null,
    termsOfService: String = null,
    contact: Contact = null,
    license: License = null,
    version: String = null,
    extensions: Map[String, AnyRef] = null
  ): Info = {
    val result = new Info()
    result.title(title)
    result.description(description)
    result.termsOfService(termsOfService)
    result.contact(contact)
    result.license(license)
    result.version(version)
    result.setExtensions(extensions.asJava)
    result
  }

  def Contact
  (
    name: String = null,
    url: String = null,
    email: String = null,
    extensions: Map[String, AnyRef] = null
  ): Contact = {
    val result = new Contact()
    result.name(name)
    result.url(url)
    result.email(email)
    result.setExtensions(extensions.asJava)
    result
  }

  def License
  (
    name: String = null,
    url: String = null,
    email: String = null,
    extensions: Map[String, AnyRef] = null
  ): License = {
    val result = new License()
    result.name(name)
    result.url(url)
    result.setExtensions(extensions.asJava)
    result
  }

  def Server
  (
    url: String,
    description: String = null,
    variables: ServerVariables = null,
    extensions: Map[String, AnyRef] = null
  ): Server = {
    val result = new Server()
    result.url(url)
    result.description(description)
    result.variables(variables)
    result.setExtensions(extensions.asJava)
    result
  }

  def ServerVariables
  (
    values: Map[String, ServerVariable],
    extensions: Map[String, AnyRef] = null
  ): ServerVariables = {
    val result = new ServerVariables()
    result.putAll(values.asJava)
    result.setExtensions(extensions.asJava)
    result
  }

  def ServerVariable
  (
    enum: Set[String] = null,
    default: String = null,
    description: String = null,
    variables: ServerVariables = null,
    extensions: Map[String, AnyRef] = null
  ): ServerVariable = {
    val result = new ServerVariable()
    result._enum(enum.toList.asJava)
    result._default(default)
    result.description(description)
    result.setExtensions(extensions.asJava)
    result
  }

  def ExternalDocumentation
  (
    description: String = null,
    url: String = null,
    extensions: Map[String, AnyRef] = null
  ): ExternalDocumentation = {
    val result = new ExternalDocumentation()
    result.description(description)
    result.url(url)
    result.setExtensions(extensions.asJava)
    result
  }

  def Tag
  (
    name: String,
    description: String = null,
    url: String = null,
    externalDocs: ExternalDocumentation = null,
    extensions: Map[String, AnyRef] = null
  ): Tag = {
    val result = new Tag()
    result.name(name)
    result.description(description)
    result.externalDocs(externalDocs)
    result.setExtensions(extensions.asJava)
    result
  }

  def SecurityRequirement
  (
    values: Map[String, List[String]] = null,
  ): SecurityRequirement = {
    val result = new SecurityRequirement()
    result.putAll(values.mapValues(_.asJava).asJava)
    result
  }

  def Operation
  (
    tags: List[String] = null,
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
  ): Operation = {
    val result = new Operation()
    result.tags(tags.asJava)
    result.summary(summary)
    result.description(description)
    result.externalDocs(externalDocs)
    result.operationId(operationId)
    result.parameters(parameters.asJava)
    result.requestBody(requestBody)
    result.responses(responses)
    result.callbacks(callbacks.asJava)
    result.deprecated(deprecated)
    result.security(security.asJava)
    result.servers(servers.asJava)
    result.extensions(extensions.asJava)
    result
  }

  def ApiResponses
  (
    default: ApiResponse,
    responses: Map[Long, ApiResponse] = null
  ): ApiResponses = {
    val result = new ApiResponses()
    result._default(default)

    if (responses != null) {
      responses.foreach(e => result.put(e._1.toString, e._2))
    }

    result
  }
}
