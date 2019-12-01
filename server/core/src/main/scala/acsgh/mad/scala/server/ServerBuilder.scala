package acsgh.mad.scala.server

import acsgh.mad.scala.server.router.http.HttpRouterBuilder
import acsgh.mad.scala.server.router.ws.WSRouterBuilder

class ServerBuilder() {

  private var _name = "Mad Server"
  private var _productionMode: Boolean = false
  private var _ipAddress: String = "0.0.0.0"
  private var _httpPort: Option[Int] = Some(6080)
  private var _httpsPort: Option[Int] = None
  private var _sslConfig: Option[SSLConfig] = None
  private var _workerThreads: Int = 30
  private var _readerIdleTimeSeconds: Int = 60
  private var _writerIdleTimeSeconds: Int = 30

  private val httpRouterBuilder: HttpRouterBuilder = new HttpRouterBuilder()
  private val wsRouterBuilder: WSRouterBuilder = new WSRouterBuilder()

  def build(): Server = Server(
    _name,
    _productionMode,
    _ipAddress,
    _httpPort,
    _httpsPort,
    _sslConfig,
    _readerIdleTimeSeconds,
    _writerIdleTimeSeconds,
    httpRouterBuilder.build(_name, _productionMode),
    wsRouterBuilder.build(_name, _productionMode)
  )

  def http: HttpRouterBuilder = httpRouterBuilder

  def ws: WSRouterBuilder = wsRouterBuilder

  def name: String = _name

  def name(value: String): Unit = {
    _name = value
  }

  def productionMode: Boolean = _productionMode

  def productionMode(value: Boolean): Unit = {
    _productionMode = value
  }

  def ipAddress: String = _ipAddress

  def ipAddress(ipAddress: String): Unit = {
    _ipAddress = ipAddress
  }


  def httpPort: Option[Int] = _httpPort

  def httpPort(port: Option[Int]): Unit = {
    _httpPort = port
  }

  def httpsPort: Option[Int] = _httpsPort

  def httpsPort(port: Option[Int]): Unit = {
    _httpsPort = port
  }

  def sslConfig: Option[SSLConfig] = _sslConfig

  def sslConfig(sslConfig: Option[SSLConfig]): Unit = {
    _sslConfig = sslConfig
  }

  def readerIdleTimeSeconds: Int = _readerIdleTimeSeconds

  def readerIdleTimeSeconds(value: Int): Unit = {
    _readerIdleTimeSeconds = value
  }

  def writerIdleTimeSeconds: Int = _writerIdleTimeSeconds

  def writerIdleTimeSeconds(value: Int): Unit = {
    _writerIdleTimeSeconds = value
  }
}
