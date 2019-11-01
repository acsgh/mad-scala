package acsgh.mad.scala

import acsgh.mad.scala.provider.NettyServerChannel
import acsgh.mad.scala.router.http.{HttpRouter, Routes}
import acsgh.mad.scala.router.ws.{WSRouter, WSRoutes}
import com.acsgh.common.scala.App

abstract class Server extends App with Routes with WSRoutes {

  private var _productionMode: Boolean = false
  private var _ipAddress: String = "0.0.0.0"
  private var _httpPort: Option[Int] = Some(6080)
  private var _httpsPort: Option[Int] = None
  private var _sslConfig: Option[SSLConfig] = None
  private var _workerThreads: Int = 30

  private var httpServer: Option[NettyServerChannel] = None
  private var httpsServer: Option[NettyServerChannel] = None

  protected override val httpRouter: HttpRouter = new HttpRouter(name, {
    _productionMode
  })

  protected val wsRouter: WSRouter = new WSRouter(name, {
    _productionMode
  })

  def productionMode(value: Boolean): Unit = {
    checkNotStarted()
    _productionMode = value
  }

  def productionMode: Boolean = _productionMode

  def httpPort: Option[Int] = _httpPort

  def httpPort(port: Option[Int]): Unit = {
    checkNotStarted()
    _httpPort = port
  }

  def httpsPort: Option[Int] = _httpsPort

  def httpsPort(port: Option[Int]): Unit = {
    checkNotStarted()
    _httpsPort = port
  }

  def sslConfig: Option[SSLConfig] = _sslConfig

  def sslConfig(sslConfig: Option[SSLConfig]): Unit = {
    checkNotStarted()
    _sslConfig = sslConfig
  }

  def workerThreads: Int = _workerThreads

  def workerThreads(workerThreads: Int): Unit = {
    checkNotStarted()
    _workerThreads = workerThreads
  }

  def ipAddress: String = _ipAddress

  def ipAddress(ipAddress: String): Unit = {
    checkNotStarted()
    _ipAddress = ipAddress
  }

  onConfigure {
    httpPort.foreach { port =>
      log.info(s"$name server is listening in http://$ipAddress:$port")
      httpServer = Some(
        new NettyServerChannel(ipAddress, port, None, httpRouter, wsRouter, _workerThreads)
      )
    }

    httpsPort.foreach { port =>
      log.info(s"$name server is listening in https://$ipAddress:$port")
      val sslContext = sslConfig.fold(SSLConfig.DEFAULT)(_.sslContext)
      httpsServer = Some(
        new NettyServerChannel(ipAddress, port, Some(sslContext), httpRouter, wsRouter, _workerThreads)
      )
    }
  }

  onStart {
    httpServer.foreach(_.start())
    httpsServer.foreach(_.start())
  }

  onStop {
    httpServer.foreach(_.stop())
    httpsServer.foreach(_.stop())
    httpServer = None
    httpsServer = None
  }

  private def checkNotStarted(): Unit = {
    if (started) {
      throw new IllegalArgumentException("This action can only be performed before start the service")
    }
  }
}