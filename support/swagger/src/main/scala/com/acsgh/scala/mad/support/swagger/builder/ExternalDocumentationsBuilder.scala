package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.ExternalDocumentation

import scala.collection.JavaConverters._

case class ExternalDocumentationsBuilder[T <: Builder[_, _]](parent: T, protected val delegate: ExternalDocumentation) extends Builder[T, ExternalDocumentation] {

  def description: String = delegate.getDescription

  def description(name: String): ExternalDocumentationsBuilder[T] = {
    delegate.setDescription(name)
    this
  }

  def url: String = delegate.getUrl

  def url(url: String): ExternalDocumentationsBuilder[T] = {
    delegate.setUrl(url)
    this
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ExternalDocumentationsBuilder[T] = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ExternalDocumentationsBuilder[T] = {
    delegate.addExtension(key, value)
    this
  }
}
