package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.exception.BadRequestException
import com.acs.scala.server.mad.router.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler}
import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

private[router] case class Route[T](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: Request): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: Request): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

trait ErrorCodeHandler extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder, responseStatus: ResponseStatus): Response
}

trait ExceptionHandler  extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder, throwable: Throwable): Response
}

sealed trait Routable

trait RequestFilter extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder, nextJump: () => Option[Response]): Option[Response]
}

trait RequestHandler extends DefaultFormats {
  def handle(request: Request, responseBuilder: ResponseBuilder): Option[Response]
}

trait MadServer extends LogSupport {

  val filters: List[Route[RequestFilter]] = List()
  val handlers: List[Route[RequestHandler]] = List()
  val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler]= Map()
  val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler()

  val host: String = "0.0.0.0"
  protected val httpPort: Option[Int] = None

  def process(httpRequest: Request): Response = {
    val responseBuilder = ResponseBuilder(this, httpRequest)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
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

  private[router] def getErrorResponse(httpRequest: Request, responseBuilder: ResponseBuilder, responseStatus: ResponseStatus): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(httpRequest, responseBuilder, responseStatus)
  }

  private def processFilters(httpRequest: Request, responseBuilder: ResponseBuilder): Option[Response] = {
    val httpRoutes = filters.filter(_.canApply(httpRequest))
    getSupplier(httpRequest, responseBuilder, httpRoutes, 0)()
  }

  private def processHandler(httpRequest: Request, responseBuilder: ResponseBuilder): Option[Response] = {
    getRequestHandler(httpRequest).flatMap { httpRoute =>
      val stopWatch = new StopWatch().start
      try {
        httpRoute.handler.handle(httpRequest.ofRoute(httpRoute), responseBuilder)
      } finally {
        stopWatch.printElapseTime("Handler " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
      }
    }
  }

  private def getSupplier(httpRequest: Request, responseBuilder: ResponseBuilder, httpRoutes: List[Route[RequestFilter]], index: Int): () => Option[Response] = () => {
    if (index < httpRoutes.size) {
      val httpRoute = httpRoutes(index)
      log.trace("Filter {} {}", Array(httpRoute.methods, httpRoute.uri): _*)
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

  private def getRequestHandler(httpRequest: Request): Option[Route[RequestHandler]] = handlers
    .filter(_.canApply(httpRequest))
    .lastOption
}
