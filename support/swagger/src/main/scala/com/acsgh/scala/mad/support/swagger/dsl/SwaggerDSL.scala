package com.acsgh.scala.mad.support.swagger.dsl

import io.swagger.v3.oas.models

import scala.collection.JavaConverters._

trait SwaggerDSL {

  implicit class OpenApiConverter(input: OpenAPI) {

    def asJava: models.OpenAPI = {
      val result = new models.OpenAPI()

      result.setOpenapi(input.openapi)

      if (input.info != null) {
        result.setInfo(input.info.asJava)
      }

      if (input.externalDocs != null) {
        result.setExternalDocs(input.externalDocs.asJava)
      }

      if (input.servers != null) {
        input.servers.foreach(s => result.addServersItem(s.asJava))
      }

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class InfoConverter(input: Info) {

    def asJava: models.info.Info = {
      val result = new models.info.Info()

      result.setTitle(input.title)
      result.setDescription(input.description)
      result.setTermsOfService(input.termsOfService)

      if (input.contact != null) {
        result.setContact(input.contact.asJava)
      }
      if (input.license != null) {
        result.setLicense(input.license.asJava)
      }
      result.setVersion(input.version)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ContactConverter(input: Contact) {

    def asJava: models.info.Contact = {
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

  implicit class LicenseConverter(input: License) {

    def asJava: models.info.License = {
      val result = new models.info.License()

      result.setName(input.name)
      result.setUrl(input.url)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ExternalDocumentationConverter(input: ExternalDocumentation) {

    def asJava: models.ExternalDocumentation = {
      val result = new models.ExternalDocumentation()

      result.setDescription(input.description)
      result.setUrl(input.url)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ServerConverter(input: Server) {

    def asJava: models.servers.Server = {
      val result = new models.servers.Server()

      result.setDescription(input.description)
      result.setUrl(input.url)

      if (input.variables != null) {
        result.variables(input.variables.asJava)
      }

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ServerVariablesConverter(input: ServerVariables) {

    def asJava: models.servers.ServerVariables = {
      val result = new models.servers.ServerVariables()

      if (input.values != null) {
        input.values.foreach(e => result.addServerVariable(e._1, e._2.asJava))
      }

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ServerVariableConverter(input: ServerVariable) {

    def asJava: models.servers.ServerVariable = {
      val result = new models.servers.ServerVariable()

      if (input.enum != null) {
        result.setEnum(input.enum.toList.asJava)
      }

      result.setDefault(input.default)
      result.setDescription(input.description)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

}
