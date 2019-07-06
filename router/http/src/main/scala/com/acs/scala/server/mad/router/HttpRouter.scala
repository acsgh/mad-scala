package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.LogSupport
import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.exception.BadRequestException
import com.acs.scala.server.mad.router.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler}
import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

private[router] case class Route[T <: Routable](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: Request): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: Request): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

trait ErrorCodeHandler extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder, responseStatus: ResponseStatus): Response
}

trait ExceptionHandler extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder, throwable: Throwable): Response
}

sealed trait Routable extends DefaultFormats

case class RequestContext
(
  request: Request,
  responseBuilder: ResponseBuilder
)

trait RequestFilter extends Routable {
  def handle(requestContext: RequestContext, nextJump: () => Response): Response
}

trait RequestHandler extends Routable {
  def handle(requestContext: RequestContext): Response
}

trait HttpRouter extends LogSupport {

  var filters: List[Route[RequestFilter]] = List()
  var handlers: List[Route[RequestHandler]] = List()
  val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler()

  private[router] def servlet(route: Route[RequestHandler]): Unit = handlers = handlers ++ List(route)

  private[router] def filter(route: Route[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    val responseBuilder = ResponseBuilder(this, httpRequest)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
    val stopWatch = new StopWatch().start()
    try {
      processFilters(httpRequest, responseBuilder)
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

  private[router] def getErrorResponse(httpRequest: Request, responseBuilder: ResponseBuilder, responseStatus: ResponseStatus): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(httpRequest, responseBuilder, responseStatus)
  }

  private def processFilters(httpRequest: Request, responseBuilder: ResponseBuilder): Response = {
    val httpRoutes = filters.filter(_.canApply(httpRequest))
    runFilter(httpRequest, responseBuilder, httpRoutes)()
  }

  private def runFilter(httpRequest: Request, responseBuilder: ResponseBuilder, httpRoutes: List[Route[RequestFilter]]): () => Response = () => {
    if (httpRoutes.nonEmpty) {
      val httpRoute = httpRoutes.head
      log.trace("Filter {} {}", Array(httpRoute.methods, httpRoute.uri): _*)
      val stopWatch = new StopWatch().start()
      try {
        httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder, runFilter(httpRequest, responseBuilder, httpRoutes.tail))
      } finally {
        stopWatch.printElapseTime("Filter " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
      }
    } else {
      processHandler(httpRequest, responseBuilder)
    }
  }

  private def processHandler(httpRequest: Request, responseBuilder: ResponseBuilder): Response = {
    handlers
      .find(_.canApply(httpRequest))
      .map { httpRoute =>
        val stopWatch = new StopWatch().start
        try {
          httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder)
        } finally {
          stopWatch.printElapseTime("Handler " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
        }
      }
      .getOrElse({
        getErrorResponse(httpRequest, responseBuilder, ResponseStatus.NOT_FOUND)
      })
  }
}
