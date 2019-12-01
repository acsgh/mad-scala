package acsgh.mad.scala.converter.json.spray

import acsgh.mad.scala.core.http.model.{RequestContext, HttpResponse, ResponseStatus}
import acsgh.mad.scala.router.http.handler.DefaultErrorCodeHandler
import acsgh.mad.scala.router.http.model.{RequestContext, Response}

class JsonErrorCodeHandler extends DefaultErrorCodeHandler with SprayDirectives with JsonErrorCodeFormat {

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
