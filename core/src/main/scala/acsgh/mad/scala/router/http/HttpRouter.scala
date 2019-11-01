package acsgh.mad.scala.router.http

import acsgh.mad.scala.URLSupport
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import acsgh.mad.scala.router.http.model._
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch

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
  def handle(nextJump: () => RouteResult)(implicit requestContext: RequestContext): RouteResult
}

final class HttpRouter(serverName: => String, _productionMode: => Boolean) extends LogSupport {

  //  protected var filters: List[HttpRoute[RequestFilter]] = List()
  protected var servlet: List[HttpRoute[Route]] = List()
  protected val errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  protected val defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  protected val exceptionHandler: ExceptionHandler = new DefaultExceptionHandler(_productionMode)

  def productionMode: Boolean = _productionMode

  private[http] def servlet(route: HttpRoute[Route]): Unit = {
    if (containsRoute(servlet, route.uri, route.methods)) {
      log.debug("The servlet method {} - {} has been already defined", Some(route.methods).filter(_.nonEmpty).map(_.mkString(", ")).getOrElse("All"), route.uri)
    } else {
      servlet = servlet ++ List(route)
    }
  }

  //  private[http] def filter(route: HttpRoute[RequestFilter]): Unit = filters = filters ++ List(route)

  def process(httpRequest: Request): Response = {
    val ctx: RequestContext = RequestContext(httpRequest, ResponseBuilder(httpRequest), this)
    log.debug(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
    ctx.response.header("Server", serverName)
    val stopWatch = StopWatch.createStarted()
    try {
      runServlet(ctx)
    } finally {
      stopWatch.printElapseTime(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code}", log, LogLevel.INFO)
    }
  }

  private[http] def getErrorResponse(responseStatus: ResponseStatus, message: Option[String] = None)(implicit context: RequestContext): RouteResult = {
    val errorCodeHandler = errorCodeHandlers.getOrElse(responseStatus, defaultErrorCodeHandler)
    errorCodeHandler.handle(responseStatus, message)
  }

  //  private def processFilters(context: RequestContext): Response = {
  //    val httpRoutes = filters.filter(_.canApply(context.request))
  //    runFilter(context, httpRoutes)()
  //  }

  private def containsRoute(routesList: List[HttpRoute[_]], uri: String, methods: Set[RequestMethod]): Boolean = {
    val routes = routesList.map(e => (e.uri, e.methods)).groupBy(_._1).view.mapValues(_.flatMap(_._2).toSet).toMap

    routes.get(uri).exists { r =>
      (r.isEmpty && methods.isEmpty) || r.intersect(methods).nonEmpty
    }
  }

  //  private def runFilter(context: RequestContext, nextFilters: List[HttpRoute[RequestFilter]]): () => Response = () => {
  //    runSafe(context) { c1 =>
  //      if (nextFilters.nonEmpty) {
  //        val currentFilter = nextFilters.head
  //        log.trace("Filter {} {}", Array(currentFilter.methods, currentFilter.uri): _*)
  //        val stopWatch = StopWatch.createStarted()
  //        try {
  //          currentFilter.handler.handle(runFilter(c1, nextFilters.tail))(c1.ofRoute(currentFilter))
  //        } finally {
  //          stopWatch.printElapseTime("Filter " + currentFilter.methods + " " + currentFilter.uri, log, LogLevel.TRACE)
  //        }
  //      } else {
  //        runSafe(c1)(runServlet)
  //      }
  //    }
  //  }

  private def runServlet(context: RequestContext): Response = {
    servlet
      .find(_.canApply(context.request))
      .map { httpRoute =>
        val stopWatch = StopWatch.createStarted()
        try {
          httpRoute.handler.apply(context.ofRoute(httpRoute))
        } finally {
          stopWatch.printElapseTime("Servlet " + httpRoute.methods + " " + httpRoute.uri, log, LogLevel.TRACE)
        }
      }
      .getOrElse({
        getErrorResponse(ResponseStatus.NOT_FOUND)(context)
      }) match {
      case Left(e) => context.response.build
      case Right(e) => e.asInstanceOf[Response]
    }
  }

  private def runSafe(ctx: RequestContext)(action: (RequestContext) => RouteResult): RouteResult = {
    try {
      action(ctx)
    } catch {
      case e: BadRequestException =>
        log.debug("Bad request", e)
        exceptionHandler.handle(e)(ctx)
      case e: Exception =>
        log.error(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", e)
        exceptionHandler.handle(e)(ctx)
    }
  }
}
