package acsgh.mad.scala

import acsgh.mad.scala.router.http.listener.RequestListener
import acsgh.mad.scala.router.http.{HttpRouter, Routes}
import acsgh.mad.scala.router.ws.listener.WSRequestListener
import acsgh.mad.scala.router.ws.{WSRouter, WSRoutes}
import com.acsgh.common.scala.App

trait ServerApp extends App with Routes with WSRoutes {

  protected var server: Server = Server()


  override protected val httpRouter: HttpRouter = server.httpRouter
  override protected val wsRouter: WSRouter = server.wsRouter

  onConfigure {
    server.name(name)
  }

  onStart {
    server.start()
  }

  onStop {
    server.stop()
  }


  def httpRequestListeners: List[RequestListener] = server.httpRequestListeners

  def addHttpRequestListeners(listener: RequestListener): Unit = server.addHttpRequestListeners(listener)

  def removeHttpRequestListeners(listener: RequestListener): Unit = server.removeHttpRequestListeners(listener)

  def wsRequestListeners: List[WSRequestListener] = server.wsRequestListeners

  def addWSRequestListeners(listener: WSRequestListener): Unit = server.addWSRequestListeners(listener)

  def removeWSRequestListeners(listener: WSRequestListener): Unit = server.removeWSRequestListeners(listener)

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

}
