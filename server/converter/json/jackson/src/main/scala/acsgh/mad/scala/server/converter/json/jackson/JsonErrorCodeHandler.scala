package acsgh.mad.scala.server.converter.json.jackson

import acsgh.mad.scala.core.http.model.{HttpResponse, ResponseStatus}
import acsgh.mad.scala.server.router.http.handler.DefaultErrorCodeHandler
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import com.fasterxml.jackson.databind.ObjectMapper
import acsgh.mad.scala.server.router.http.body.writer.default._

class JsonErrorCodeHandler(implicit objectMapper: ObjectMapper) extends DefaultErrorCodeHandler with JacksonDirectives {


  override def handle(status: ResponseStatus, message: Option[String])(implicit requestContext: HttpRequestContext): HttpResponse = {
    requestHeader("Accept".opt, "Content-Type".opt) { (accept, contentType) =>
      responseStatus(status) {
        if (isJson(accept) || isJson(contentType)) {
          responseJson(JsonErrorCode(
            status.code,
            status.message,
            message
          ))
        } else {
          responseBody(getStatusBody(status, message))
        }
      }
    }
  }

  private def isJson(header: Option[String]): Boolean = header match {
    case Some(s) if s.toLowerCase.contains("json") => true
    case _ => false
  }
}
