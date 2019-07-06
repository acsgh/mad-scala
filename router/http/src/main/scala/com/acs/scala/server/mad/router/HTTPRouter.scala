package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.exception.BadRequestException
import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

private[router] case class HTTPRoute[T <: Routable](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: HTTPRequest): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: HTTPRequest): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

trait ErrorCodeHandler {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, responseStatus: ResponseStatus): HTTPResponse
}

trait ExceptionHandler {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, throwable: Throwable): HTTPResponse
}

sealed trait Routable

trait RequestFilter extends Routable {
  def handle(request: HTTPRequest, responseBuilder: HTTPResponseBuilder, nextJump: () => Option[HTTPResponse]): Option[HTTPResponse]
}

trait RequestHandler extends Routable {
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
    val responseBuilder = HTTPResponseBuilder(this, httpRequest)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri):_*)
    val stopWatch = new StopWatch().start()
    try {
      processFilters(httpRequest, responseBuilder)
        .getOrElse({
          processHandler(httpRequest, responseBuilder)
            .getOrElse({
              getErrorResponse(httpRequest, responseBuilder, ResponseStatus.NOT_FOUND)
            })
        })
    } catch {
      case e: BadRequestException =>
        log.debug("Invalid Parameter", e)
        getErrorResponse(httpRequest, responseBuilder, ResponseStatus.BAD_REQUEST)
      case e: Exception =>
        log.error("Error during request", e)
        exceptionHandler.handle(httpRequest, responseBuilder, e)
    } finally {
      stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri, log, LogLevel.DEBUG)
    }
  }

  private[router] def getErrorResponse(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder, responseStatus: ResponseStatus): HTTPResponse = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(httpRequest, responseBuilder, responseStatus)
  }

  private def processFilters(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder): Option[HTTPResponse] = {
    val httpRoutes = filters.filter(_.canApply(httpRequest))
    getSupplier(httpRequest, responseBuilder, httpRoutes, 0)()
  }

  private def processHandler(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder): Option[HTTPResponse] = {
    getRequestHandler(httpRequest).flatMap { httpRoute =>
      val stopWatch = new StopWatch().start
      try {
        httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder)
      } finally {
        stopWatch.printElapseTime("Handler " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
      }
    }
  }

  private def getSupplier(httpRequest: HTTPRequest, responseBuilder: HTTPResponseBuilder, httpRoutes: List[HTTPRoute[RequestFilter]], index: Int): () => Option[HTTPResponse] = () => {
    if (index < httpRoutes.size) {
      val httpRoute = httpRoutes(index)
      log.trace("Filter {} {}", Array(httpRoute.methods, httpRoute.uri):_*)
      val stopWatch = new StopWatch().start()
      try {
        httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder, getSupplier(httpRequest, responseBuilder, httpRoutes, index + 1))
      } finally {
        stopWatch.printElapseTime("Filter " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
      }
    } else {
      None
    }
  }

  private def getRequestHandler(httpRequest: HTTPRequest): Option[HTTPRoute[RequestHandler]] = handlers
    .filter(_.canApply(httpRequest))
    .lastOption
}
