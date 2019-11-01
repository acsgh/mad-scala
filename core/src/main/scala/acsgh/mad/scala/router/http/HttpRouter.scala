package acsgh.mad.scala.router.http

import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import acsgh.mad.scala.router.http.model.{Route, _}
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch


final class HttpRouter(serverName: => String, _productionMode: => Boolean) extends LogSupport {

  protected var filters: List[Route[FilterAction]] = List()
  protected var servlet: List[Route[RouteAction]] = List()
  protected val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  protected val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  protected val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler(_productionMode)

  def productionMode: Boolean = _productionMode

  def process(httpRequest: Request): Response = {
    implicit val ctx: RequestContext = model.RequestContext(httpRequest, ResponseBuilder(httpRequest), this)
    log.debug(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
    ctx.response.header("Server", serverName)
    val stopWatch = StopWatch.createStarted()
    try {
      runSafe(runServlet)(ctx)
    } finally {
      stopWatch.printElapseTime(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code}", log, LogLevel.INFO)
    }
  }

  private[http] def servlet(route: Route[RouteAction]): Unit = {
    if (containsRoute(servlet, route.uri, route.methods)) {
      log.debug("The servlet method {} - {} has been already defined", Some(route.methods).filter(_.nonEmpty).map(_.mkString(", ")).getOrElse("All"), route.uri)
    } else {
      servlet = servlet ++ List(route)
    }
  }

  private[http] def filter(route: Route[FilterAction]): Unit = filters = filters ++ List(route)

  private[http] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: RequestContext): Response = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(responseStatus, message)
  }

  private def containsRoute(routesList: List[Route[_]], uri: String, methods: Set[RequestMethod]): Boolean = {
    val routes = routesList.map(e => (e.uri, e.methods)).groupBy(_._1).view.mapValues(_.flatMap(_._2).toSet).toMap

    routes.get(uri).exists { r =>
      (r.isEmpty && methods.isEmpty) || r.intersect(methods).nonEmpty
    }
  }

  private def runServlet(context: RequestContext): Response = {
    servlet
      .find(_.canApply(context.request))
      .map(r => runRoute(r, context))
      .getOrElse({
        getErrorResponse(ResponseStatus.NOT_FOUND)(context)
      })
  }

  private def runRoute(route: Route[RouteAction], context: RequestContext): Response = {
    val stopWatch = StopWatch.createStarted()
    try {
      implicit val ctx: RequestContext = context.ofRoute(route)
      val filtersToExecute = filters.filter(_.canApply(context.request))
      runFilters(route, filtersToExecute)
      route.action.apply(ctx)
    } finally {
      stopWatch.printElapseTime("Servlet " + route.methods + " " + route.uri, log, LogLevel.TRACE)
    }
  }

  private def runFilters(route: Route[RouteAction], nextFilters: List[Route[FilterAction]])(implicit context: RequestContext): Response = {
    runSafe { c1 =>
      if (nextFilters.nonEmpty) {
        val currentFilter = nextFilters.head
        log.trace("Filter {} {}", Array(currentFilter.methods, currentFilter.uri): _*)
        val stopWatch = StopWatch.createStarted()
        try {
          currentFilter.action(c1)(_ => runFilters(route, nextFilters.tail))
        } finally {
          stopWatch.printElapseTime("Filter " + currentFilter.methods + " " + currentFilter.uri, log, LogLevel.TRACE)
        }
      } else {
        runSafe(route.action)
      }
    }
  }


  private def runSafe(action: RequestContext => Response)(implicit ctx: RequestContext): Response = {
    try {
      action(ctx)
    } catch {
      case e: BadRequestException =>
        log.debug("Bad request", e)
        exceptionHandler.handle(e)
      case e: Exception =>
        log.error(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", e)
        exceptionHandler.handle(e)
    }
  }
}
