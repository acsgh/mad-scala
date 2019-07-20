package com.acsgh.scala.mad.support.swagger.dsl.converter

import com.acsgh.scala.mad.support.swagger.dsl.model._
import io.swagger.v3.oas.models

import scala.collection.JavaConverters._

trait CommonConverter {

  implicit class ExternalDocumentationConverter(val input: ExternalDocumentation) extends Converter[ExternalDocumentation, models.ExternalDocumentation] {

    def asJavaNullSafe: models.ExternalDocumentation = {
      val result = new models.ExternalDocumentation()

      result.setDescription(input.description)
      result.setUrl(input.url)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class TagConverter(val input: Tag) extends Converter[Tag, models.tags.Tag] {

    def asJavaNullSafe: models.tags.Tag = {
      val result = new models.tags.Tag()

      result.setName(input.name)
      result.setDescription(input.description)
      result.setExternalDocs(input.externalDocs.asJava)


      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

}
