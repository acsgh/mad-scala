package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.callbacks.Callback
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.headers.Header
import io.swagger.v3.oas.models.links.Link
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.{Parameter, RequestBody}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityScheme

import scala.collection.JavaConverters._

case class ComponentsBuilder(parent: OpenApiBuilder, protected val delegate: Components) extends Builder[OpenApiBuilder, Components] {

  def callbacks(key: String, callbacksItem: Callback): ComponentsBuilder = {
    delegate.addCallbacks(key, callbacksItem)
    this
  }

  def callbacks: Map[String, Callback] = Map() ++ delegate.getCallbacks.asScala

  def links(key: String, linksItem: Link): ComponentsBuilder = {
    delegate.addLinks(key, linksItem)
    this
  }

  def links: Map[String, Link] = Map() ++ delegate.getLinks.asScala

  def securitySchemes(key: String, securitySchemesItem: SecurityScheme): ComponentsBuilder = {
    delegate.addSecuritySchemes(key, securitySchemesItem)
    this
  }

  def securitySchemes: Map[String, SecurityScheme] = Map() ++ delegate.getSecuritySchemes.asScala

  def headers(key: String, headersItem: Header): ComponentsBuilder = {
    delegate.addHeaders(key, headersItem)
    this
  }

  def headers: Map[String, Header] = Map() ++ delegate.getHeaders.asScala

  def requestBodies(key: String, requestBodiesItem: RequestBody): ComponentsBuilder = {
    delegate.addRequestBodies(key, requestBodiesItem)
    this
  }

  def requestBodies: Map[String, RequestBody] = Map() ++ delegate.getRequestBodies.asScala

  def examples(key: String, examplesItem: Example): ComponentsBuilder = {
    delegate.addExamples(key, examplesItem)
    this
  }

  def examples: Map[String, Example] = Map() ++ delegate.getExamples.asScala

  def parameters(key: String, parametersItem: Parameter): ComponentsBuilder = {
    delegate.addParameters(key, parametersItem)
    this
  }

  def parameters: Map[String, Parameter] = Map() ++ delegate.getParameters.asScala

  def responses(key: String, responsesItem: ApiResponse): ComponentsBuilder = {
    delegate.addResponses(key, responsesItem)
    this
  }

  def responses: Map[String, ApiResponse] = Map() ++ delegate.getResponses.asScala

  def schemas(key: String, schemasItem: Schema[_]): ComponentsBuilder = {
    delegate.addSchemas(key, schemasItem)
    this
  }

  def schemas: Map[String, Schema[_]] = Map() ++ delegate.getSchemas.asScala

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ComponentsBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ComponentsBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
