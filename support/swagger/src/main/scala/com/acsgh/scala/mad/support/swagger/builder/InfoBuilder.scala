package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.info.{Contact, Info, License}

import scala.collection.JavaConverters._

case class InfoBuilder(parent: OpenApiBuilder, protected val delegate: Info) extends Builder[OpenApiBuilder, Info] {

  def title(title: String): InfoBuilder = {
    delegate.setTitle(title)
    this
  }

  def title: String = delegate.getTitle

  def description(description: String): InfoBuilder = {
    delegate.setDescription(description)
    this
  }

  def description: String = delegate.getDescription

  def version(version: String): InfoBuilder = {
    delegate.setVersion(version)
    this
  }

  def version: String = delegate.getVersion

  def termsOfService(termsOfService: String): Unit = delegate.setTermsOfService(termsOfService)

  def termsOfService: String = delegate.getTermsOfService

  def contact: ContactBuilder = {
    if (delegate.getContact == null) {
      delegate.setContact(new Contact())
    }
    ContactBuilder(this, delegate.getContact)
  }

  def license: LicenseBuilder = {
    if (delegate.getLicense == null) {
      delegate.setLicense(new License())
    }
    LicenseBuilder(this, delegate.getLicense)
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): InfoBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): InfoBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
