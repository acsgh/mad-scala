package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.ws.listener.WSRequestListener
import acsgh.mad.scala.router.ws.model._
import com.acsgh.common.scala.log.LogSupport

class WSRouterBuilder() extends WSRoutes with LogSupport {

  private var _workerThreads: Int = 30
  private var _workerTimeoutSeconds: Int = 60
  private var _wsRoutes: Map[String, WSRoute] = Map()
  private var _defaultHandler: WSRouteAction = ctx => Some(WSResponseText("Unknown route"))
  private var _requestListeners: List[WSRequestListener] = List()

  def workerThreads: Int = _workerThreads

  def workerThreads(value: Int): Unit = {
    _workerThreads = value
  }

  def workerTimeoutSeconds: Int = _workerTimeoutSeconds

  def workerTimeoutSeconds(value: Int): Unit = {
    _workerTimeoutSeconds = value
  }

  def defaultHandler(value: WSRouteAction): Unit = {
    _defaultHandler = value
  }

  def requestListeners: List[WSRequestListener] = _requestListeners

  def addRequestListeners(value: WSRequestListener): Unit = {
    _requestListeners = _requestListeners ++ List(value)
  }

  def removeRequestListeners(value: WSRequestListener): Unit = {
    _requestListeners = _requestListeners.filterNot(_ == value)
  }

  override protected def route(uri: String, subprotocols: Set[String])(handler: WSRouteAction): Unit = _wsRoutes = _wsRoutes + (uri -> WSRoute(subprotocols, handler))

  def build(serverName: String, productionMode: Boolean): WSRouter = WSRouter(
    serverName,
    productionMode,
    _workerThreads: Int,
    _workerTimeoutSeconds: Int,
    _wsRoutes,
    _defaultHandler,
    _requestListeners,
  )
}
