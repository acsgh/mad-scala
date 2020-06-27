package acsgh.mad.scala.server.router.http.params

import acsgh.mad.scala.server.router.http.body.reader.multipart.Multipart
import acsgh.mad.scala.server.router.http.body.reader.urlFormEncoded.UrlFormEncodedBody
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.reader.HttpParamReader
import acsgh.mad.scala.server.router.http.params.reader.default._

trait HttpDefaultParamHandling {


  implicit def string2Param(name: String)(implicit reader: HttpParamReader[String, String]): Param[String, String, String] = SingleParam[String, String](name)

  implicit class StringParamsEnhanced(name: String) {
    def as[T](implicit reader: HttpParamReader[String, T]): SingleParam[String, T] = SingleParam[String, T](name)

    def opt: OptionParam[String, String] = OptionParam[String, String](name)

    def default(defaultValue: String): DefaultParam[String, String] = DefaultParam[String, String](name, defaultValue)

    def list: ListParam[String, String] = ListParam[String, String](name)
  }

  implicit class SingleParamEnhanced[I, P](param: SingleParam[I, P])(implicit reader: HttpParamReader[I, P]) {
    def opt: OptionParam[I, P] = OptionParam[I, P](param.name)

    def default(defaultValue: P): DefaultParam[I, P] = DefaultParam[I, P](param.name, defaultValue)

    def list: ListParam[I, P] = ListParam[I, P](param.name)
  }

  implicit class ParamsEnhanced[O, R](param: Param[String, O, R]) {
    def multipartValue(implicit context: HttpRequestContext, bodyContent: Multipart): R = {
      val value = bodyContent.parts.find(_.name.equalsIgnoreCase(param.name)).map(part => List(part.content)).getOrElse(List())
      param("Multipart", value)
    }

    def formValue(implicit context: HttpRequestContext, bodyContent: UrlFormEncodedBody): R = {
      val value = bodyContent.params.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())
      param("Form", value)
    }

    def queryValue(implicit context: HttpRequestContext): R = {
      val value = context.request.queryParams.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())
      param("Query", value)
    }

    def pathValue(implicit context: HttpRequestContext): R = {
      val value = context.pathParams.find(_._1.equalsIgnoreCase(param.name)).map(_._2).toList
      param("Path", value)
    }

    def cookieValue(implicit context: HttpRequestContext): R = {
      val value = context.request.cookieParams.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())
      param("Cookie", value)
    }

    def headerValue(implicit context: HttpRequestContext): R = {
      val value = context.request.headers.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())
      param("Header", value)
    }
  }

}
