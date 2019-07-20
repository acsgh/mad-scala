package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.servers.{ServerVariable, ServerVariables}

import scala.collection.JavaConverters._

case class ServerVariablesBuilder(parent: ServerBuilder, protected val delegate: ServerVariables) extends Builder[ServerBuilder, ServerVariables] {

  def variable(name: String): ServerVariableBuilder = {
    if (!delegate.containsKey(name)) {
      delegate.put(name, new ServerVariable())
    }

    ServerVariableBuilder(this, delegate.get(name))
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ServerVariablesBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ServerVariablesBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
