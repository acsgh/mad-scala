package acsgh.mad.scala.server.router.http.directives

import java.net.URLDecoder

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.server.router.http.convertions.{HttpBodyReader, HttpDefaultFormats, HttpDefaultParamHandling}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext


trait HttpRequestFormDirectives extends HttpDefaultParamHandling with HttpDefaultFormats with HttpRequestBody {

  implicit private object UrlFormEncodedBodyReader extends HttpBodyReader[UrlFormEncodedBody] {

    override val contentTypes: Set[String] = Set("application/x-www-form-urlencoded")

    override val strictContentTypes: Boolean = true

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): UrlFormEncodedBody = {
      val rawContent = new String(body, "UTF-8")
      val params = rawContent.split("&", -1)
        .flatMap { param =>
          val paramParts = param.split("=", -1)

          if (paramParts.length > 1) {
            val key = URLDecoder.decode(paramParts(0), "UTF-8")
            val value = URLDecoder.decode(paramParts(1), "UTF-8")
            Option(key -> value)
          } else {
            None
          }
        }.groupBy { case (key, _) => key }
        .view
        .mapValues(_.map { case (_, value) => value }.toList)
        .toMap

      UrlFormEncodedBody(params)

    }
  }

  def requestFormParam[P1, R1](param1: Param[P1, R1])(action: R1 => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2](param1: Param[P1, R1], param2: Param[P2, R2])(action: (R1, R2) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3])(action: (R1, R2, R3) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4])(action: (R1, R2, R3, R4) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5])(action: (R1, R2, R3, R4, R5) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6])(action: (R1, R2, R3, R4, R5, R6) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7])(action: (R1, R2, R3, R4, R5, R6, R7) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8])(action: (R1, R2, R3, R4, R5, R6, R7, R8) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13], param14: Param[P14, R14])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue, param14.formValue)
    }
  }

  def requestFormParam[P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14, P15, R15](param1: Param[P1, R1], param2: Param[P2, R2], param3: Param[P3, R3], param4: Param[P4, R4], param5: Param[P5, R5], param6: Param[P6, R6], param7: Param[P7, R7], param8: Param[P8, R8], param9: Param[P9, R9], param10: Param[P10, R10], param11: Param[P11, R11], param12: Param[P12, R12], param13: Param[P13, R13], param14: Param[P14, R14], param15: Param[P15, R15])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15) => HttpResponse)(implicit context: HttpRequestContext): HttpResponse = {
    requestBody[UrlFormEncodedBody] { implicit bodyContent =>
      action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue, param14.formValue, param15.formValue)
    }
  }
}