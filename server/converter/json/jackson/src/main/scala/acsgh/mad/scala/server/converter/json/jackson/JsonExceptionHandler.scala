package acsgh.mad.scala.server.converter.json.jackson

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.core.http.model.ResponseStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import acsgh.mad.scala.server.router.http.handler.DefaultExceptionHandler
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import com.fasterxml.jackson.databind.ObjectMapper

class JsonExceptionHandler(implicit objectMapper: ObjectMapper) extends DefaultExceptionHandler with JacksonDirectives {

  override def handle(throwable: Throwable)(implicit ctx: HttpRequestContext): HttpResponse = {
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
