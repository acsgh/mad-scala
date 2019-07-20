package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.info.License

import scala.collection.JavaConverters._

case class LicenseBuilder(parent: InfoBuilder, protected val delegate: License) extends Builder[InfoBuilder, License] {

  def name: String = delegate.getName

  def name(name: String): LicenseBuilder = {
    delegate.setName(name)
    this
  }

  def url: String = delegate.getUrl

  def url(url: String): LicenseBuilder = {
    delegate.setUrl(url)
    this
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): LicenseBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): LicenseBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
