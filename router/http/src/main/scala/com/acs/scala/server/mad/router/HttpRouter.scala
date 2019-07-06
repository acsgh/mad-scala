package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.LogSupport
import com.acs.scala.server.mad.router.directives.Directives
import com.acs.scala.server.mad.router.exception.BadRequestException
import com.acs.scala.server.mad.router.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import com.acs.scala.server.mad.router.model._
import com.acs.scala.server.mad.utils.{LogLevel, StopWatch}

case class RequestContext
(
  request: Request,
  response: ResponseBuilder,
  router: HttpRouter,
  route: Option[HttpRoute[_]] = None
) {
  def ofRoute(newRoute: HttpRoute[_]): RequestContext = copy(route = Some(newRoute))
}

trait RequestFilter extends Directives {
  def handle(requestContext: RequestContext, nextJump: () => Response): Response
}

trait RequestHandler extends Directives {
  def handle(requestContext: RequestContext): Response
}

trait HttpRouter extends LogSupport {

  var filters: List[HttpRoute[RequestFilter]] = List()
  var servlet: List[HttpRoute[RequestHandler]] = List()
  val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler()

  private[router] def servlet(route: HttpRoute[RequestHandler]): Unit = servlet = servlet ++ List(route)

  private[router] def filter(route: HttpRoute[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    implicit val context: RequestContext = RequestContext(httpRequest, ResponseBuilder(this, httpRequest), this)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
    val stopWatch = new StopWatch().start()
    try {
      processFilters(context)
    } catch {
      case e: BadRequestException =>
        log.debug("Invalid Parameter", e)
        getErrorResponse(ResponseStatus.BAD_REQUEST, Some(e.message))
      case e: Exception =>
        log.error("Error during request", e)
        exceptionHandler.handle(e)
    } finally {
      stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri, log, LogLevel.DEBUG)
    }
  }

  private[router] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: RequestContext): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(responseStatus, message)
  }

  private def processFilters(context: RequestContext): Response = {
    val httpRoutes = filters.filter(_.canApply(context.request))
    runFilter(context, httpRoutes)()
  }

  private def runFilter(context: RequestContext, nextFilters: List[HttpRoute[RequestFilter]]): () => Response = () => {
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
      runServlet()(context)
    }
  }

  private def runServlet()(implicit context: RequestContext): Response = {
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
        getErrorResponse(ResponseStatus.NOT_FOUND)
      })
  }
}
