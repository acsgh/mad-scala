package com.acs.scala.server.mad.router.handler

import com.acs.scala.server.mad.router.constant.ResponseStatus
import com.acs.scala.server.mad.router.{ErrorCodeHandler, RequestContext, Response}

class DefaultErrorCodeHandler extends ErrorCodeHandler {
  override def handle(status: ResponseStatus)(implicit requestContext: RequestContext): Response = {
    responseStatus(status) {
      responseBody(getStatusBody(status))
    }
  }

  private def getStatusBody(status: ResponseStatus): String = {
    s"""<html>
       |<head>
       |   <title>${status.code} - ${status.message}</title>
       |</head>
       |<body>
       |   <h1>${status.code} - ${status.message}</h1>
       |</body>
       |</html>""".stripMargin
  }
}
