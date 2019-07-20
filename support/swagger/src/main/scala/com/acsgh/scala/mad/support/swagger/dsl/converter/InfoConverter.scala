package com.acsgh.scala.mad.support.swagger.dsl.converter

import com.acsgh.scala.mad.support.swagger.dsl.model._
import io.swagger.v3.oas.models

import scala.collection.JavaConverters._

trait InfoConverter {

  implicit class InfoConverter(val input: Info) extends Converter[Info, models.info.Info] {

    def asJavaNullSafe: models.info.Info = {
      val result = new models.info.Info()

      result.setTitle(input.title)
      result.setDescription(input.description)
      result.setTermsOfService(input.termsOfService)

      result.setContact(input.contact.asJava)
      result.setLicense(input.license.asJava)
      result.setVersion(input.version)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ContactConverter(val input: Contact) extends Converter[Contact, models.info.Contact] {

    def asJavaNullSafe: models.info.Contact = {
      val result = new models.info.Contact()

      result.setName(input.name)
      result.setUrl(input.url)
      result.setEmail(input.email)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class LicenseConverter(val input: License) extends Converter[License, models.info.License] {

    def asJavaNullSafe: models.info.License = {
      val result = new models.info.License()

      result.setName(input.name)
      result.setUrl(input.url)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }
}
