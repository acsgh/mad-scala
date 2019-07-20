package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.servers.ServerVariable

import scala.collection.JavaConverters._

case class ServerVariableBuilder(parent: ServerVariablesBuilder, protected val delegate: ServerVariable) extends Builder[ServerVariablesBuilder, ServerVariable] {


  def description(description: String): ServerVariableBuilder = {
    delegate.setDescription(description)
    this
  }

  def description: String = delegate.getDescription

  def default(input: String): ServerVariableBuilder = {
    delegate.setDefault(input)
    this
  }

  def default: String = delegate.getDefault

  def enum(items: String*): ServerVariableBuilder = {
    delegate.setEnum(items.toList.asJava)
    this
  }

  def enum: List[String] = List() ++ delegate.getEnum.asScala

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ServerVariableBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ServerVariableBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
