package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.servers.{Server, ServerVariables}

import scala.collection.JavaConverters._

case class ServerBuilder(parent: OpenApiBuilder, protected val delegate: Server) extends Builder[OpenApiBuilder, Server] {

  def description: String = delegate.getDescription

  def description(description: String): ServerBuilder = {
    delegate.setDescription(description)
    this
  }

  def url: String = delegate.getUrl

  private[builder] def url(url: String): ServerBuilder = {
    delegate.setUrl(url)
    this
  }

  def variables: ServerVariablesBuilder = {
    if (delegate.getVariables == null) {
      delegate.setVariables(new ServerVariables())
    }
    ServerVariablesBuilder(this, delegate.getVariables)
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ServerBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ServerBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
