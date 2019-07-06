package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.LogSupport
import com.acs.scala.server.mad.router.constant.{RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.directives.DefaultParamHandling
import com.acs.scala.server.mad.router.exception.BadRequestException
import com.acs.scala.server.mad.router.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler}
import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

private[router] case class Route[T <: Routable](uri: String, methods: Set[RequestMethod], handler: T) {
  private[router] def canApply(httpRequest: Request): Boolean = validMethod(httpRequest) && httpRequest.address.matchUrl(this.uri)

  private def validMethod(httpRequest: Request): Boolean = methods.isEmpty || methods.contains(httpRequest.method)
}

trait ErrorCodeHandler extends DefaultFormats with DefaultParamHandling {
  def handle(requestContext: RequestContext, responseStatus: ResponseStatus): Response
}

trait ExceptionHandler extends DefaultFormats with DefaultParamHandling {
  def handle(requestContext: RequestContext, throwable: Throwable): Response
}

sealed trait Routable extends DefaultFormats with DefaultParamHandling

case class RequestContext
(
  request: Request,
  responseBuilder: ResponseBuilder
) {
  def ofRoute(httpRoute: Route[_]): RequestContext = copy(request = request.ofRoute(httpRoute))
}

trait RequestFilter extends Routable {
  def handle(requestContext: RequestContext, nextJump: () => Response): Response
}

trait RequestHandler extends Routable {
  def handle(requestContext: RequestContext): Response
}

trait HttpRouter extends LogSupport {

  var filters: List[Route[RequestFilter]] = List()
  var servlet: List[Route[RequestHandler]] = List()
  val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler()

  private[router] def servlet(route: Route[RequestHandler]): Unit = servlet = servlet ++ List(route)

  private[router] def filter(route: Route[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    val context = RequestContext(httpRequest, ResponseBuilder(this, httpRequest))
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
    val stopWatch = new StopWatch().start()
    try {
      processFilters(context)
    } catch {
      case e: BadRequestException =>
        log.debug("Invalid Parameter", e)
        getErrorResponse(context, ResponseStatus.BAD_REQUEST)
      case e: Exception =>
        log.error("Error during request", e)
        exceptionHandler.handle(context, e)
    } finally {
      stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri, log, LogLevel.DEBUG)
    }
  }

  private[router] def getErrorResponse(context: RequestContext, responseStatus: ResponseStatus): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(context, responseStatus)
  }

  private def processFilters(context: RequestContext): Response = {
    val httpRoutes = filters.filter(_.canApply(context.request))
    runFilter(context, httpRoutes)()
  }

  private def runFilter(context: RequestContext, nextFilters: List[Route[RequestFilter]]): () => Response = () => {
    if (nextFilters.nonEmpty) {
      val currentFilter = nextFilters.head
      log.trace("Filter {} {}", Array(currentFilter.methods, currentFilter.uri): _*)
      val stopWatch = new StopWatch().start()
      try {
        currentFilter.handler.handle(context.ofRoute(currentFilter), runFilter(context, nextFilters.tail))
      } finally {
        stopWatch.printElapseTime("Filter " + currentFilter.methods + " " + currentFilter.uri, log, LogLevel.TRACE)
      }
    } else {
      runServlet(context)
    }
  }

  private def runServlet(context: RequestContext): Response = {
    servlet
      .find(_.canApply(context.request))
      .map { httpRoute =>
        val stopWatch = new StopWatch().start
        try {
          httpRoute.handler.handle(context.ofRoute(httpRoute))
        } finally {
          stopWatch.printElapseTime("Servlet " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
        }
      }
      .getOrElse({
        getErrorResponse(context, ResponseStatus.NOT_FOUND)
      })
  }
}
