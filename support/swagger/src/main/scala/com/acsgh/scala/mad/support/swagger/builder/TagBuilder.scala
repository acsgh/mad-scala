package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.tags.Tag

import scala.collection.JavaConverters._

case class TagBuilder[T <: Builder[_, _]](parent: T, protected val delegate: Tag) extends Builder[T, Tag] {

  def name(title: String): TagBuilder[T] = {
    delegate.setName(title)
    this
  }

  def name: String = delegate.getName

  def description(description: String): TagBuilder[T] = {
    delegate.setDescription(description)
    this
  }

  def description: String = delegate.getDescription


  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): TagBuilder[T] = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): TagBuilder[T] = {
    delegate.addExtension(key, value)
    this
  }
}
