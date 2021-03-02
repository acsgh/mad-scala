package acsgh.mad.scala.server.router.http

import acsgh.mad.scala.core.http.model.ResponseStatus
import acsgh.mad.scala.server.router.http.handler.{DefaultErrorCodeHandler, DefaultExceptionHandler, ErrorCodeHandler, ExceptionHandler}
import acsgh.mad.scala.server.router.http.listener.RequestListener
import acsgh.mad.scala.server.router.http.model.{Route, _}
import com.acsgh.common.scala.log.LogSupport

class HttpRouterBuilder() extends HttpRoutes with LogSupport {

  private var _workerThreads: Int = 30
  private var _workerTimeoutSeconds: Int = 60
  private var _filters: List[Route[HttpFilterAction]] = List()
  private var _servlet: List[Route[HttpRouteAction]] = List()
  private var _errorCodeHandlers: Map[ResponseStatus, ErrorCodeHandler] = Map()
  private var _defaultErrorCodeHandler: ErrorCodeHandler = new DefaultErrorCodeHandler()
  private var _exceptionHandler: ExceptionHandler = new DefaultExceptionHandler()
  private var _requestListeners: List[RequestListener] = List()

  def workerThreads: Int = _workerThreads

  def workerThreads(value: Int): Unit = {
    _workerThreads = value
  }

  def workerTimeoutSeconds: Int = _workerTimeoutSeconds

  def workerTimeoutSeconds(value: Int): Unit = {
    _workerTimeoutSeconds = value
  }

  def errorCodeHandlers(status: ResponseStatus, handler: ErrorCodeHandler): Unit = {
    _errorCodeHandlers = _errorCodeHandlers + (status -> handler)
  }

  def defaultErrorCodeHandler(value: ErrorCodeHandler): Unit = {
    _defaultErrorCodeHandler = value
  }

  def exceptionHandler(value: ExceptionHandler): Unit = {
    _exceptionHandler = value
  }

  def requestListeners: List[RequestListener] = _requestListeners

  def addRequestListeners(value: RequestListener): Unit = {
    _requestListeners = _requestListeners ++ List(value)
  }

  def removeRequestListeners(value: RequestListener): Unit = {
    _requestListeners = _requestListeners.filterNot(_ == value)
  }

  override def servlet(route: Route[HttpRouteAction]): Unit = _servlet = _servlet ++ List(route)

  override def filter(route: Route[HttpFilterAction]): Unit = _filters = _filters ++ List(route)

  def build(serverName: String, productionMode: Boolean): HttpRouter = HttpRouter(
    serverName,
    productionMode,
    _workerThreads,
    _workerTimeoutSeconds,
    _filters,
    _servlet,
    _errorCodeHandlers,
    _defaultErrorCodeHandler,
    _exceptionHandler,
    _requestListeners,
  )
}
