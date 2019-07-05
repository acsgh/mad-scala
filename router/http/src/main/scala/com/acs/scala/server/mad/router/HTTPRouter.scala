package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}

case class HTTPRoute[T](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: HTTPRequest): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: HTTPRequest): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

class HTTPRouter {
  def process(httpRequest: HTTPRequest): HTTPResponse = {
    ???
  }

  private[router] def getErrorResponse(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder, responseStatus: ResponseStatus) = {
    ???
  }
}
