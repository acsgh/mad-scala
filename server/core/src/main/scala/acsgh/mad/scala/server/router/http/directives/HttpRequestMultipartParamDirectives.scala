package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.server.router.http.body.reader.multipart._
import acsgh.mad.scala.server.router.http.convertions.{HttpDefaultFormats, HttpDefaultParamHandling}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext


trait HttpRequestMultipartParamDirectives extends HttpDefaultParamHandling with HttpDefaultFormats with HttpRequestBody {

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
