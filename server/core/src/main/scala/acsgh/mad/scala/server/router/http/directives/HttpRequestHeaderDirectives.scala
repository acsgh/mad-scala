package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.{HttpDefaultParamHandling, HttpParam}

object HttpRequestHeaderDirectives extends HttpRequestHeaderDirectives

trait HttpRequestHeaderDirectives extends HttpDefaultParamHandling with HttpDirectivesBase {

  def requestHeader[R, P1, R1](param1: HttpParam[String, P1, R1])(action: R1 => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2])(action: (R1, R2) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3])(action: (R1, R2, R3) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4])(action: (R1, R2, R3, R4) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5])(action: (R1, R2, R3, R4, R5) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6])(action: (R1, R2, R3, R4, R5, R6) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7])(action: (R1, R2, R3, R4, R5, R6, R7) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8])(action: (R1, R2, R3, R4, R5, R6, R7, R8) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10], param11: HttpParam[String, P11, R11])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue, param11.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10], param11: HttpParam[String, P11, R11], param12: HttpParam[String, P12, R12])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue, param11.headerValue, param12.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10], param11: HttpParam[String, P11, R11], param12: HttpParam[String, P12, R12], param13: HttpParam[String, P13, R13])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue, param11.headerValue, param12.headerValue, param13.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10], param11: HttpParam[String, P11, R11], param12: HttpParam[String, P12, R12], param13: HttpParam[String, P13, R13], param14: HttpParam[String, P14, R14])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue, param11.headerValue, param12.headerValue, param13.headerValue, param14.headerValue)
  }

  def requestHeader[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14, P15, R15](param1: HttpParam[String, P1, R1], param2: HttpParam[String, P2, R2], param3: HttpParam[String, P3, R3], param4: HttpParam[String, P4, R4], param5: HttpParam[String, P5, R5], param6: HttpParam[String, P6, R6], param7: HttpParam[String, P7, R7], param8: HttpParam[String, P8, R8], param9: HttpParam[String, P9, R9], param10: HttpParam[String, P10, R10], param11: HttpParam[String, P11, R11], param12: HttpParam[String, P12, R12], param13: HttpParam[String, P13, R13], param14: HttpParam[String, P14, R14], param15: HttpParam[String, P15, R15])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15) => R)(implicit context: HttpRequestContext): R = {
    action(param1.headerValue, param2.headerValue, param3.headerValue, param4.headerValue, param5.headerValue, param6.headerValue, param7.headerValue, param8.headerValue, param9.headerValue, param10.headerValue, param11.headerValue, param12.headerValue, param13.headerValue, param14.headerValue, param15.headerValue)
  }
}
