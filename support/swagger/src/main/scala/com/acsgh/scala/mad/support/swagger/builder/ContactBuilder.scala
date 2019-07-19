package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.info.Contact

case class ContactBuilder(parent: InfoBuilder, private val delegate: Contact) {

  val root: OpenApiBuilder = parent.root

  def name: String = delegate.getName

  def name(name: String): ContactBuilder = {
    delegate.setName(name)
    this
  }

  def url: String = delegate.getUrl

  def url(url: String): ContactBuilder = {
    delegate.setUrl(url)
    this
  }

  def email: String = delegate.getEmail

  def email(email: String): ContactBuilder = {
    delegate.setEmail(email)
    this
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): ContactBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): ContactBuilder = {
    delegate.addExtension(key, value)
    this
  }
}
