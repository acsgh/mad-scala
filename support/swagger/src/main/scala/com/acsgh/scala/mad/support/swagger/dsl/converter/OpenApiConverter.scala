package com.acsgh.scala.mad.support.swagger.dsl.converter

import com.acsgh.scala.mad.support.swagger.dsl.model._
import io.swagger.v3.oas.models

import scala.collection.JavaConverters._

trait OpenApiConverter extends InfoConverter with CommonConverter with ServerConverter {

  implicit class OpenApiConverter(val input: OpenAPI) extends Converter[OpenAPI, models.OpenAPI] {

    def asJavaNullSafe: models.OpenAPI = {
      val result = new models.OpenAPI()

      result.setOpenapi(input.openapi)
      result.setInfo(input.info.asJava)
      result.setExternalDocs(input.externalDocs.asJava)

      if (input.servers != null) {
        input.servers.foreach(s => result.addServersItem(s.asJava))
      }

      if (input.security != null) {
        input.security.foreach(s => result.addSecurityItem(s.asJava))
      }

      if (input.tags != null) {
        input.tags.foreach(t => result.addTagsItem(t.asJava))
      }

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class SecurityRequirementConverter(val input: SecurityRequirement) extends Converter[SecurityRequirement, models.security.SecurityRequirement] {

    def asJavaNullSafe: models.security.SecurityRequirement = {
      val result = new models.security.SecurityRequirement()

      input.foreach { e =>
        result.put(e._1, e._2.asJava)
      }

      result
    }
  }

}
