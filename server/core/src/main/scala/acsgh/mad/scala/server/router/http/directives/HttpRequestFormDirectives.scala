package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.body.reader.multipart.{Multipart, _}
import acsgh.mad.scala.server.router.http.body.reader.urlFormEncoded._
import acsgh.mad.scala.server.router.http.directives.HttpRequestBody._
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.{HttpDefaultParamHandling, HttpParam}

import scala.reflect.ClassTag


object HttpRequestFormDirectives extends HttpRequestFormDirectives

trait HttpRequestFormDirectives extends HttpDefaultParamHandling with HttpDirectivesBase {

  def formParam[R, I1: ClassTag, O1: ClassTag, R1](param1: HttpParam[I1, O1, R1])(action: R1 => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2])(action: (R1, R2) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3])(action: (R1, R2, R3) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4])(action: (R1, R2, R3, R4) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5])(action: (R1, R2, R3, R4, R5) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6])(action: (R1, R2, R3, R4, R5, R6) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7])(action: (R1, R2, R3, R4, R5, R6, R7) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8])(action: (R1, R2, R3, R4, R5, R6, R7, R8) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10, I11: ClassTag, O11: ClassTag, R11](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10], param11: HttpParam[I11, O11, R11])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10, I11: ClassTag, O11: ClassTag, R11, I12: ClassTag, O12: ClassTag, R12](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10], param11: HttpParam[I11, O11, R11], param12: HttpParam[I12, O12, R12])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10, I11: ClassTag, O11: ClassTag, R11, I12: ClassTag, O12: ClassTag, R12, I13: ClassTag, O13: ClassTag, R13](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10], param11: HttpParam[I11, O11, R11], param12: HttpParam[I12, O12, R12], param13: HttpParam[I13, O13, R13])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10, I11: ClassTag, O11: ClassTag, R11, I12: ClassTag, O12: ClassTag, R12, I13: ClassTag, O13: ClassTag, R13, I14: ClassTag, O14: ClassTag, R14](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10], param11: HttpParam[I11, O11, R11], param12: HttpParam[I12, O12, R12], param13: HttpParam[I13, O13, R13], param14: HttpParam[I14, O14, R14])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue, param14.formValue)
      }
    }
  }

  def formParam[R, I1: ClassTag, O1: ClassTag, R1, I2: ClassTag, O2: ClassTag, R2, I3: ClassTag, O3: ClassTag, R3, I4: ClassTag, O4: ClassTag, R4, I5: ClassTag, O5: ClassTag, R5, I6: ClassTag, O6: ClassTag, R6, I7: ClassTag, O7: ClassTag, R7, I8: ClassTag, O8: ClassTag, R8, I9: ClassTag, O9: ClassTag, R9, I10: ClassTag, O10: ClassTag, R10, I11: ClassTag, O11: ClassTag, R11, I12: ClassTag, O12: ClassTag, R12, I13: ClassTag, O13: ClassTag, R13, I14: ClassTag, O14: ClassTag, R14, I15: ClassTag, O15: ClassTag, R15](param1: HttpParam[I1, O1, R1], param2: HttpParam[I2, O2, R2], param3: HttpParam[I3, O3, R3], param4: HttpParam[I4, O4, R4], param5: HttpParam[I5, O5, R5], param6: HttpParam[I6, O6, R6], param7: HttpParam[I7, O7, R7], param8: HttpParam[I8, O8, R8], param9: HttpParam[I9, O9, R9], param10: HttpParam[I10, O10, R10], param11: HttpParam[I11, O11, R11], param12: HttpParam[I12, O12, R12], param13: HttpParam[I13, O13, R13], param14: HttpParam[I14, O14, R14], param15: HttpParam[I15, O15, R15])(action: (R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, R13, R14, R15) => R)(implicit context: HttpRequestContext): R = {
    requestBodyOpt[UrlFormEncodedBody, R] { implicit urlFormEncodedBody =>
      requestBodyOpt[Multipart, R] { implicit multipart =>
        action(param1.formValue, param2.formValue, param3.formValue, param4.formValue, param5.formValue, param6.formValue, param7.formValue, param8.formValue, param9.formValue, param10.formValue, param11.formValue, param12.formValue, param13.formValue, param14.formValue, param15.formValue)
      }
    }
  }
}
