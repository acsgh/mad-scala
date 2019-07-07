package com.acs.scala.server.mad.router.convertions

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.directives._

trait DefaultParamHandling {

  implicit object StringWriter extends ParamWriter[String] {
    override def write(input: String): String = input
  }

  implicit object StringReader extends ParamReader[String] {
    override def read(input: String): String = input
  }

  implicit object LongWriter extends ParamWriter[Long] {
    override def write(input: Long): String = input.toString
  }

  implicit object LongReader extends ParamReader[Long] {
    override def read(input: String): Long = input.toLong
  }

  implicit def string2Param(name: String)(implicit reader: ParamReader[String]): Param[String, String] = SingleParam[String](name)

  implicit class StringParamsEnhanced(name: String) {
    def as[T](implicit reader: ParamReader[T]): SingleParam[T] = SingleParam[T](name)

    def opt: OptionParam[String] = OptionParam[String](name)

    def default(defaultValue: String): DefaultParam[String] = DefaultParam[String](name, defaultValue)

    def list: ListParam[String] = ListParam[String](name)
  }

  implicit class SingleParamEnhanced[P](param: SingleParam[P])(implicit reader: ParamReader[P]) {
    def opt: OptionParam[P] = OptionParam[P](param.name)

    def default(defaultValue: P): DefaultParam[P] = DefaultParam[P](param.name, defaultValue)

    def list: ListParam[P] = ListParam[P](param.name)
  }

  implicit class ParamsEnhanced[P, R](param: Param[P, R]) {
    def queryValue(implicit context: RequestContext): R = {
      val value = context.request.queryParams.getOrElse(param.name, List())
      param("Query", value)
    }

    def pathValue(implicit context: RequestContext): R = {
      val value = context.pathParams.get(param.name).toList
      param("Path", value)
    }

    def headerValue(implicit context: RequestContext): R = {
      val value = context.request.headers.getOrElse(param.name, List())
      param("Header", value)
    }
  }

}
