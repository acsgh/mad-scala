package acsgh.mad.scala.server.router.http.handler

import acsgh.mad.scala.core.http.model.{HttpResponse, ResponseStatus}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

class DefaultErrorCodeHandler extends ErrorCodeHandler {
  override def handle(status: ResponseStatus, message: Option[String])(implicit requestContext: HttpRequestContext): HttpResponse = {
    responseStatus(status) {
      responseBody(getStatusBody(status, message))
    }
  }

  protected def getStatusBody(status: ResponseStatus, message: Option[String]): String = {
    s"""<html>
       |<head>
       |   <title>${status.code} - ${status.message}</title>
       |</head>
       |<body>
       |   <h1>${status.code} - ${status.message}</h1>
       |   ${message.fold("")(m => s"""<p>$m</p>""")}
       |</body>
       |</html>""".stripMargin
  }
}
