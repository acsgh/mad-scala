package com.acsgh.scala.mad.router.http

import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.exception.BadRequestException
import com.acsgh.scala.mad.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import com.acsgh.scala.mad.router.http.model.{Request, Response, ResponseBuilder, ResponseStatus}
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch
import com.acsgh.scala.mad.{ProductionInfo, URLSupport}

case class RequestContext
(
  request: Request,
  response: ResponseBuilder,
  router: HttpRouter,
  route: Option[HttpRoute[_]] = None
) extends URLSupport {
  def ofRoute(newRoute: HttpRoute[_]): RequestContext = copy(route = Some(newRoute))

  val pathParams: Map[String, String] = route.map(r => extractPathParams(r.uri, request.uri)).getOrElse(Map())
}

trait RequestFilter extends LogSupport with Directives {
  def handle(nextJump: () => Response)(implicit requestContext: RequestContext): Response
}

trait RequestServlet extends LogSupport with Directives {
  def handle(implicit requestContext: RequestContext): Response
}

trait HttpRouter extends LogSupport with ProductionInfo {

  protected var filters: List[HttpRoute[RequestFilter]] = List()
  protected var servlet: List[HttpRoute[RequestServlet]] = List()
  protected val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  protected val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  protected val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler({productionMode})

  private[http] def servlet(route: HttpRoute[RequestServlet]): Unit = servlet = servlet ++ List(route)

  private[http] def filter(route: HttpRoute[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    implicit val context: RequestContext = RequestContext(httpRequest, ResponseBuilder(httpRequest), this)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
    val stopWatch = StopWatch.createStarted()
    try {
      processFilters(context)
    } catch {
      case e: BadRequestException =>
        log.debug("Bad request", e)
        exceptionHandler.handle(e)
      case e: Exception =>
        log.error("Error during request", e)
        exceptionHandler.handle(e)
    } finally {
      stopWatch.printElapseTime("Request " + httpRequest.method + " " + httpRequest.uri, log, LogLevel.DEBUG)
    }
  }

  private[http] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: RequestContext): Response = {
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
      val stopWatch = StopWatch.createStarted()
      try {
        currentFilter.handler.handle(runFilter(context, nextFilters.tail))(context.ofRoute(currentFilter))
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
        val stopWatch = StopWatch.createStarted()
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
