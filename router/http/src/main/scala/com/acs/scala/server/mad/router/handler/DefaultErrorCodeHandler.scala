package com.acs.scala.server.mad.router.handler

import com.acs.scala.server.mad.router.constant.ResponseStatus
import com.acs.scala.server.mad.router.{ErrorCodeHandler, Request, Response, ResponseBuilder}

class DefaultErrorCodeHandler extends ErrorCodeHandler {
  override def handle(request: Request, responseBuilder: ResponseBuilder, status: ResponseStatus): Response = {
    responseBuilder.status(status)
    responseBuilder.body(getStatusBody(status))
    responseBuilder.build
  }

  private def getStatusBody(status: ResponseStatus): String = {
    s"""<html>
       |<head>
       |   <title>${status.code } - ${status.message}</title>
       |</head>
       |<body>
       |   <h1>${status.code } - ${status.message}</h1>
       |</body>
       |</html>""".stripMargin
  }
}
