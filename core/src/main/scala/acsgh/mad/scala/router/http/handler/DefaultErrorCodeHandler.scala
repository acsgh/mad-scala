package acsgh.mad.scala.router.http.handler

import acsgh.mad.scala.router.http.model.{RequestContext, Response, ResponseStatus}

class DefaultErrorCodeHandler extends ErrorCodeHandler {
  override def handle(status: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): Response = {
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
