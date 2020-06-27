package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.{HttpDefaultParamHandling, Param}

object HttpRequestPathParamsDirectives extends HttpRequestPathParamsDirectives

trait HttpRequestPathParamsDirectives extends HttpDefaultParamHandling with HttpDefaultFormats {

  def pathParam[R, P1, R1](param1: Param[String, P1, R1])(action: R1 => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2](param1: Param[String, P1, R1], param2: Param[String, P2, R2])(action: (R1, R2) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3])(action: (R1, R2, R3) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4])(action: (R1, R2, R3, R4) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5])(action: (R1, R2, R3, R4, R5) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6])(action: (R1, R2, R3, R4, R5, R6) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7])(action: (R1, R2, R3, R4, R5, R6, R7) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8])(action: (R1, R2, R3, R4, R5, R6, R7, R8) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10], param11: Param[String, P11, R11])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue, param11.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10], param11: Param[String, P11, R11], param12: Param[String, P12, R12])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue, param11.pathValue, param12.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10], param11: Param[String, P11, R11], param12: Param[String, P12, R12], param13: Param[String, P13, R13])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue, param11.pathValue, param12.pathValue, param13.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10], param11: Param[String, P11, R11], param12: Param[String, P12, R12], param13: Param[String, P13, R13], param14: Param[String, P14, R14])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue, param11.pathValue, param12.pathValue, param13.pathValue, param14.pathValue)
  }

  def pathParam[R, P1, R1, P2, R2, P3, R3, P4, R4, P5, R5, P6, R6, P7, R7, P8, R8, P9, R9, P10, R10, P11, R11, P12, R12, P13, R13, P14, R14, P15, R15](param1: Param[String, P1, R1], param2: Param[String, P2, R2], param3: Param[String, P3, R3], param4: Param[String, P4, R4], param5: Param[String, P5, R5], param6: Param[String, P6, R6], param7: Param[String, P7, R7], param8: Param[String, P8, R8], param9: Param[String, P9, R9], param10: Param[String, P10, R10], param11: Param[String, P11, R11], param12: Param[String, P12, R12], param13: Param[String, P13, R13], param14: Param[String, P14, R14], param15: Param[String, P15, R15])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15) => R)(implicit context: HttpRequestContext): R = {
    action(param1.pathValue, param2.pathValue, param3.pathValue, param4.pathValue, param5.pathValue, param6.pathValue, param7.pathValue, param8.pathValue, param9.pathValue, param10.pathValue, param11.pathValue, param12.pathValue, param13.pathValue, param14.pathValue, param15.pathValue)
  }
}
