package acsgh.mad.scala.converter.json.spray

import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.handler.DefaultExceptionHandler
import acsgh.mad.scala.router.http.model.ResponseStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import acsgh.mad.scala.router.http.model.{RequestContext, Response}

class JsonExceptionHandler extends DefaultExceptionHandler with SprayDirectives with JsonErrorCodeFormat {

  override def handle(throwable: Throwable)(implicit ctx: RequestContext): Response = {
    val status = if (throwable.isInstanceOf[BadRequestException]) BAD_REQUEST else INTERNAL_SERVER_ERROR

    requestHeader("Accept".opt, "Content-Type".opt) { (accept, contentType) =>
      responseStatus(status) {
        if (isJson(accept) || isJson(contentType)) {
          responseJson(JsonErrorCode(
            status.code,
            status.message,
            Some(DefaultExceptionHandler.stacktraceToPlain(throwable)).filter(_ => !ctx.router.productionMode)
          ))
        } else {
          responseBody(getStatusBody(status, throwable))
        }
      }
    }
  }

  private def isJson(header: Option[String]): Boolean = header match {
    case Some(s) if s.toLowerCase.contains("json") => true
    case _ => false
  }
}
