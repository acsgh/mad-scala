package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}

private[router] case class HTTPRoute[T](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: HTTPRequest): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: HTTPRequest): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

trait ErrorCodeHandler {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, responseStatus: ResponseStatus): HTTPResponse
}

trait ExceptionHandler {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, throwable: Throwable): HTTPResponse
}

trait RequestFilter {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, nextJump: () => Option[HTTPResponse]): Option[HTTPResponse]
}

trait RequestHandler {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder): Option[HTTPResponse]
}

case class HTTPRouter
(
  filters: List[HTTPRoute[RequestFilter]],
  handlers: List[HTTPRoute[RequestHandler]],
  errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler],
  defaultErrorCodeHandler: ErrorCodeHandler,
  exceptionHandler: ExceptionHandler,
)
  extends LogSupport {
  def process(httpRequest: HTTPRequest): HTTPResponse = {
    ???
  }

  private[router] def getErrorResponse(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder, responseStatus: ResponseStatus) = {
    ???
  }
}
