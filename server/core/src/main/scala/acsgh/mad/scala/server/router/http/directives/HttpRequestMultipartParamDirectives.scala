package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.server.router.http.convertions.{HttpBodyReader, HttpDefaultFormats, HttpDefaultParamHandling}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext


trait HttpRequestMultipartParamDirectives extends HttpDefaultParamHandling with HttpDefaultFormats with HttpRequestBody {

  implicit private object MultipartBodyReader extends HttpBodyReader[Multipart] {

    final val EndLine = "\r\n"

    override val contentTypes: Set[String] = Set("multipart/form-data")

    override val strictContentTypes: Boolean = true

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): Multipart = {
      val separator = "; boundary="
      val contentType = context.request.headers("Content-Type").head
      val boundary = contentType.substring(contentType.lastIndexOf(separator) + separator.length)
      val rawContent = new String(body, "UTF-8")

      val endBoundary = s"$boundary--$EndLine"

      if (rawContent.endsWith(endBoundary)) {
        val parts = rawContent.substring(0, rawContent.length - endBoundary.length)
          .split(s"--$boundary$EndLine", -1)
          .filter(_.nonEmpty)
          .map { entryContent =>
            val entryPart = entryContent.split(s"$EndLine$EndLine", -1)

            val headers = entryPart(0).split(EndLine)
              .map { h =>
                (h.substring(0, h.indexOf(":")) -> h.substring(h.indexOf(":") + 2))
              }
              .toMap


            val content = entryPart(1)
              .substring(0, entryPart(1).length - EndLine.size)

            val contentDisposition = headers.getOrElse("Content-Disposition", {
              throw new BadRequestException("The multipart body is not properly formatted")
            })

            MultipartPart(
              name = extractAttr(contentDisposition, "name").getOrElse {
                throw new BadRequestException("The multipart body is not properly formatted")
              },
              contentType = headers.get("Content-Type"),
              filename = extractAttr(contentDisposition, "filename").filter(_.nonEmpty),
              content = content
            )
          }
          .toList

        Multipart(
          parts = parts
        )

      } else {
        throw new BadRequestException("The multipart body does not end properly")
      }

      //      val params = rawContent.split("&", -1)
      //        .flatMap { param =>
      //          val paramParts = param.split("=", -1)
      //
      //          if (paramParts.length > 1) {
      //            val key = URLDecoder.decode(paramParts(0), "UTF-8")
      //            val value = URLDecoder.decode(paramParts(1), "UTF-8")
      //            Option(key -> value)
      //          } else {
      //            None
      //          }
      //        }.groupBy { case (key, _) => key }
      //        .mapValues(_.map { case (_, value) => value }.toList)
      //        .toMap
      //
      //      Multipart(params)


    }

    private def extractAttr(src: String, attrName: String): Option[String] = {
      val pattern = s"""$attrName="(\\w*)"""".r
      val iterator = pattern.findAllIn(src)
      Option(iterator).filter(_ => pattern.findFirstIn(src).isDefined).map(_.group(1))
    }
  }

  def requestMultipartParam[P1, R1](param1: Param[P1, R1])(action: R1 => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2](param1: Param[P1, R1], param2: Param[P2, R2])(action: (R1, R2) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3])(action: (R1, R2, R3) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4])(action: (R1, R2, R3, R4) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5])(action: (R1, R2, R3, R4, R5) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6])(action: (R1, R2, R3, R4, R5, R6) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7])(action: (R1, R2, R3, R4, R5, R6, R7) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8])(action: (R1, R2, R3, R4, R5, R6, R7, R8) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue, param11.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue, param11.multipartValue, param12.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue, param11.multipartValue, param12.multipartValue, param13.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13], param14: Param[P14, R14])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue, param11.multipartValue, param12.multipartValue, param13.multipartValue, param14.multipartValue)
    }
  }

  def requestMultipartParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14, P15, R15](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13], param14: Param[P14, R14], param15: Param[P15, R15])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[Multipart] { implicit bodyContent =>
      action(param1.multipartValue, param2.multipartValue, param3.multipartValue, param4.multipartValue, param5.multipartValue, param6.multipartValue, param7.multipartValue, param8.multipartValue, param9.multipartValue, param10.multipartValue, param11.multipartValue, param12.multipartValue, param13.multipartValue, param14.multipartValue, param15.multipartValue)
    }
  }
}
