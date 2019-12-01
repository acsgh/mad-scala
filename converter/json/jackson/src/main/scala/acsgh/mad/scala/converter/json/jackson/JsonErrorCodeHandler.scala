package acsgh.mad.scala.converter.json.jackson

import acsgh.mad.scala.core.http.model.{RequestContext, HttpResponse, ResponseStatus}
import acsgh.mad.scala.router.http.handler.DefaultErrorCodeHandler
import acsgh.mad.scala.router.http.model.{RequestContext, Response}
import com.fasterxml.jackson.databind.ObjectMapper

class JsonErrorCodeHandler(implicit objectMapper: ObjectMapper) extends DefaultErrorCodeHandler with JacksonDirectives {


  override def handle(status: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): HttpResponse = {
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
