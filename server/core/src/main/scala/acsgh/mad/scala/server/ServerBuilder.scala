package acsgh.mad.scala.server

import acsgh.mad.scala.server.router.http.HttpRouterBuilder
import acsgh.mad.scala.server.router.ws.WSRouterBuilder

abstract class ServerBuilder() extends Controller {

  protected var _name = "Mad Server"
  protected var _productionMode: Boolean = false
  protected var _ipAddress: String = "0.0.0.0"
  protected var _httpPort: Option[Int] = Some(6080)
  protected var _httpsPort: Option[Int] = None
  protected var _sslConfig: Option[SSLConfig] = None
  protected var _readerIdleTimeSeconds: Int = 60
  protected var _writerIdleTimeSeconds: Int = 30

  protected val httpRouterBuilder: HttpRouterBuilder = new HttpRouterBuilder()
  protected val wsRouterBuilder: WSRouterBuilder = new WSRouterBuilder()

  protected val builder: ServerBuilder = this

  def build(): Server

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
