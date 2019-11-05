package acsgh.mad.scala

import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.listener.RequestListener
import acsgh.mad.scala.router.http.model.{FilterAction, RequestMethod, RouteAction}
import acsgh.mad.scala.router.ws.directives
import acsgh.mad.scala.router.ws.listener.WSRequestListener
import acsgh.mad.scala.router.ws.model.WSRouteAction

trait ServerDelegate[T <: Server] extends Directives with directives.WSDirectives {

  protected var server: T

  def httpRequestListeners: List[RequestListener] = server.httpRequestListeners

  def addHttpRequestListeners(listener: RequestListener): Unit = server.addHttpRequestListeners(listener)

  def removeHttpRequestListeners(listener: RequestListener): Unit = server.removeHttpRequestListeners(listener)

  def wsRequestListeners: List[WSRequestListener] = server.wsRequestListeners

  def addWSRequestListeners(listener: WSRequestListener): Unit = server.addWSRequestListeners(listener)

  def removeWSRequestListeners(listener: WSRequestListener): Unit = server.removeWSRequestListeners(listener)

  def name(value: String): Unit = server.name(value)

  def productionMode(value: Boolean): Unit = server.productionMode(value)

  def productionMode: Boolean = server.productionMode

  def httpPort: Option[Int] = server.httpPort

  def httpPort(port: Option[Int]): Unit = server.httpPort(port)

  def httpsPort: Option[Int] = server.httpsPort

  def httpsPort(port: Option[Int]): Unit = server.httpsPort(port)

  def sslConfig: Option[SSLConfig] = server.sslConfig

  def sslConfig(sslConfig: Option[SSLConfig]): Unit = server.sslConfig(sslConfig)

  def workerThreads: Int = server.workerThreads

  def workerThreads(workerThreads: Int): Unit = server.workerThreads(workerThreads)

  def readerIdleTimeSeconds: Int = server.readerIdleTimeSeconds

  def readerIdleTimeSeconds(value: Int): Unit = server.readerIdleTimeSeconds(value)

  def writerIdleTimeSeconds: Int = server.writerIdleTimeSeconds

  def writerIdleTimeSeconds(value: Int): Unit = server.writerIdleTimeSeconds(value)

  def httpWorkerTimeoutSeconds: Int = server.httpWorkerTimeoutSeconds

  def httpWorkerTimeoutSeconds(value: Int): Unit = server.httpWorkerTimeoutSeconds(value)

  def wsWorkerTimeoutSeconds: Int = server.wsWorkerTimeoutSeconds

  def wsWorkerTimeoutSeconds(value: Int): Unit = server.wsWorkerTimeoutSeconds(value)

  def ipAddress: String = server.ipAddress

  def ipAddress(ipAddress: String): Unit = server.ipAddress(ipAddress)

  def options(uri: String)(action: RouteAction): Unit = server.options(uri)(action)

  def get(uri: String)(action: RouteAction): Unit = server.get(uri)(action)

  def head(uri: String)(action: RouteAction): Unit = server.head(uri)(action)

  def post(uri: String)(action: RouteAction): Unit = server.post(uri)(action)

  def put(uri: String)(action: RouteAction): Unit = server.put(uri)(action)

  def patch(uri: String)(action: RouteAction): Unit = server.patch(uri)(action)

  def delete(uri: String)(action: RouteAction): Unit = server.delete(uri)(action)

  def trace(uri: String)(action: RouteAction): Unit = server.trace(uri)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: FilterAction): Unit = server.filter(uri, methods)(action)

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = server.resourceFolder(uri, resourceFolderPath)

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = server.filesystemFolder(uri, resourceFolderPath)

  def webjars(): Unit = server.webjars()

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRouteAction): Unit = server.ws(uri, subprotocols)(action)
}
