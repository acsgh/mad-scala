package com.acs.scala.server.mad.router.handler

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.model.{Response, ResponseStatus}

class DefaultErrorCodeHandler extends ErrorCodeHandler {
  override def handle(status: ResponseStatus, message: Option[String])(implicit requestContext: RequestContext): Response = {
    responseStatus(status) {
      responseBody(getStatusBody(status, message))
    }
  }

  private def getStatusBody(status: ResponseStatus, message: Option[String]): String = {
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
