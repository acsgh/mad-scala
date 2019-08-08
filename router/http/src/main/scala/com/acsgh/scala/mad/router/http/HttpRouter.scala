package com.acsgh.scala.mad.router.http

import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.exception.BadRequestException
import com.acsgh.scala.mad.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import com.acsgh.scala.mad.router.http.model._
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
  protected val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler({
    productionMode
  })

  private[http] def servlet(route: HttpRoute[RequestServlet]): Unit = {
    if (containsRoute(servlet, route.uri, route.methods)) {
      log.debug("The servlet method {} - {} has been already defined", Some(route.methods).filter(_.nonEmpty).map(_.mkString(", ")).getOrElse("All"), route.uri)
    } else {
      servlet = servlet ++ List(route)
    }
  }

  private[http] def filter(route: HttpRoute[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    val context: RequestContext = RequestContext(httpRequest, ResponseBuilder(httpRequest), this)
    log.trace("Request {} {}", Array(httpRequest.method, httpRequest.uri): _*)
    val stopWatch = StopWatch.createStarted()
    try {
      runSafe(context)(processFilters)
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

  private def containsRoute(routesList: List[HttpRoute[_]], uri: String, methods: Set[RequestMethod]): Boolean = {
    val routes = routesList.map(e => (e.uri, e.methods)).groupBy(_._1).mapValues(_.flatMap(_._2).toSet)

    routes.get(uri).exists { r =>
      (r.isEmpty && methods.isEmpty) || r.intersect(methods).nonEmpty
    }
  }

  private def runFilter(context: RequestContext, nextFilters: List[HttpRoute[RequestFilter]]): () => Response = () => {
    runSafe(context) { c1 =>
      if (nextFilters.nonEmpty) {
        val currentFilter = nextFilters.head
        log.trace("Filter {} {}", Array(currentFilter.methods, currentFilter.uri): _*)
        val stopWatch = StopWatch.createStarted()
        try {
          currentFilter.handler.handle(runFilter(c1, nextFilters.tail))(c1.ofRoute(currentFilter))
        } finally {
          stopWatch.printElapseTime("Filter " + currentFilter.methods + " " + currentFilter.uri, log, LogLevel.TRACE)
        }
      } else {
        runSafe(c1)(runServlet)
      }
    }
  }

  private def runServlet(context: RequestContext): Response = {
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
        getErrorResponse(ResponseStatus.NOT_FOUND)(context)
      })
  }

  private def runSafe(context: RequestContext)(action: (RequestContext) => Response): Response = {
    try {
      action(context)
    } catch {
      case e: BadRequestException =>
        log.debug("Bad request", e)
        exceptionHandler.handle(e)(context)
      case e: Exception =>
        log.error("Error during request", e)
        exceptionHandler.handle(e)(context)
    }
  }
}
