package com.acs.scala.server.mad.router.directives

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.convertions.{BodyReader, DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.exception.UnexpectedContentTypeException
import com.acs.scala.server.mad.router.model.Response

object Main {
  def main(args: Array[String]): Unit = {

    (1 to 15).foreach { i =>
      var generics = List[String]()
      var params = List[String]()
      var action = List[String]()
      var result = List[String]()

      (1 to i).foreach { k =>
        generics = generics ++ List(s"P$k, R$k")
        params = params ++ List(s"param$k: Param[P$k, R$k]")
        action = action ++ List(s"R$k")
        result = result ++ List(s"param$k.pathValue")
      }

      println(
        s"""def requestParam[${generics.mkString(",")}](${params.mkString(",")})(action: (${action.mkString(",")}) => Response)(implicit context: RequestContext): Response = {
           |    action(${result.mkString(",")})
           |}
         """.stripMargin)
    }
  }
}

trait RequestDirectives extends DefaultParamHandling with DefaultFormats with RequestParamsDirectives with RequestHeaderDirectives with RequestQueryDirectives {

  def requestBody(action: Array[Byte] => Response)(implicit context: RequestContext): Response = action(context.request.bodyBytes)

  def requestBody[T](action: T => Response)(implicit context: RequestContext, reader: BodyReader[T]): Response = {
    requestHeader("Content-Type") { contentType =>

      if (!context.request.validContentType(reader.contentTypes, contentType)) {
        throw new UnexpectedContentTypeException(contentType)
      }

      action(reader.read(context.request.bodyBytes))
    }
  }
}
