package com.acsgh.scala.mad.support.swagger.dsl.converter

import com.acsgh.scala.mad.support.swagger.dsl.model._
import io.swagger.v3.oas.models

import scala.collection.JavaConverters._

trait ServerConverter {

  implicit class ServerConverter(val input: Server) extends Converter[Server, models.servers.Server] {

    def asJavaNullSafe: models.servers.Server = {
      val result = new models.servers.Server()

      result.setDescription(input.description)
      result.setUrl(input.url)
      result.variables(input.variables.asJava)

      if (input.extensions != null) {
        result.setExtensions(input.extensions.asJava)
      }

      result
    }
  }

  implicit class ServerVariablesConverter(val input: ServerVariables) extends Converter[ServerVariables, models.servers.ServerVariables] {

    def asJavaNullSafe: models.servers.ServerVariables = {
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

  implicit class ServerVariableConverter(val input: ServerVariable) extends Converter[ServerVariable, models.servers.ServerVariable] {

    def asJavaNullSafe: models.servers.ServerVariable = {
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
