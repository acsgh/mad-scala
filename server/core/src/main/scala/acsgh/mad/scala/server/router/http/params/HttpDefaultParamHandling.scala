package acsgh.mad.scala.server.router.http.params

import acsgh.mad.scala.server.router.http.body.reader.multipart.{Multipart, MultipartFile, MultipartPart}
import acsgh.mad.scala.server.router.http.body.reader.urlFormEncoded.UrlFormEncodedBody
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.reader.HttpParamReader
import acsgh.mad.scala.server.router.http.params.reader.default._

import scala.language.implicitConversions
import scala.reflect.ClassTag

trait HttpDefaultParamHandling {

  implicit def string2Param(name: String)(implicit reader: HttpParamReader[String, String]): HttpParam[String, String, String] = SingleHttpParam[String, String](name)

  implicit class StringParamsEnhanced(name: String) {
    def as[T](implicit reader: HttpParamReader[String, T]): SingleHttpParam[String, T] = SingleHttpParam[String, T](name)

    def multipartFile(implicit reader: HttpParamReader[MultipartPart, MultipartFile]): SingleHttpParam[MultipartPart, MultipartFile] = SingleHttpParam[MultipartPart, MultipartFile](name)

    def opt: OptionHttpParam[String, String] = OptionHttpParam[String, String](name)

    def default(defaultValue: String): DefaultHttpParam[String, String] = DefaultHttpParam[String, String](name, defaultValue)

    def list: ListHttpParam[String, String] = ListHttpParam[String, String](name)
  }

  implicit class SingleParamEnhanced[I, P](param: SingleHttpParam[I, P])(implicit reader: HttpParamReader[I, P]) {
    def opt: OptionHttpParam[I, P] = OptionHttpParam[I, P](param.name)

    def default(defaultValue: P): DefaultHttpParam[I, P] = DefaultHttpParam[I, P](param.name, defaultValue)

    def list: ListHttpParam[I, P] = ListHttpParam[I, P](param.name)
  }

  implicit class ParamsEnhanced[O, R](param: HttpParam[String, O, R]) {
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

  implicit class FormParamsEnhanced[I, O, R](param: HttpParam[I, O, R])(implicit inputClass: ClassTag[I]) {
    def formValue(implicit context: HttpRequestContext, urlFormEncodedBody: Option[UrlFormEncodedBody], multipart: Option[Multipart]): R = {
      if (classOf[String] == inputClass.runtimeClass) {
        val queryValues = context.request.queryParams.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())
        val urlFormValues = urlFormEncodedBody.map(_.params.find(_._1.equalsIgnoreCase(param.name)).map(_._2).getOrElse(List())).getOrElse(List())
        val multipartValues = multipart
          .map(_.parts.find(_.name.equalsIgnoreCase(param.name)).filter(_.contentType.isEmpty).map(part => List(new String(part.content, "UTF-8"))).getOrElse(List()))
          .getOrElse(List())

        val values = queryValues ++ urlFormValues ++ multipartValues
        param("Form", values.asInstanceOf[List[I]])
      } else if (classOf[MultipartPart] == inputClass.runtimeClass) {
        val multipartValues = multipart
          .map(_.parts.find(_.name.equalsIgnoreCase(param.name)).filter(_.contentType.nonEmpty).map(part => List(part)).getOrElse(List()))
          .getOrElse(List())

        param("Form", multipartValues.asInstanceOf[List[I]])
      } else {
        throw new IllegalArgumentException(s"Class ${inputClass.runtimeClass.getName} not supported")
      }
    }
  }

}
