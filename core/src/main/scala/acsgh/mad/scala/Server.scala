package acsgh.mad.scala

import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.provider.NettyServer
import acsgh.mad.scala.router.http.listener.RequestListener
import acsgh.mad.scala.router.http.{HttpRouter, Routes}
import acsgh.mad.scala.router.ws.listener.WSRequestListener
import acsgh.mad.scala.router.ws.{WSRouter, WSRoutes}

case class Server() extends Routes with WSRoutes {

  private var _name = "Mad Server"
  private var _productionMode: Boolean = false
  private var _ipAddress: String = "0.0.0.0"
  private var _httpPort: Option[Int] = Some(6080)
  private var _httpsPort: Option[Int] = None
  private var _sslConfig: Option[SSLConfig] = None
  private var _workerThreads: Int = 30
  private var _readerIdleTimeSeconds: Int = 60
  private var _writerIdleTimeSeconds: Int = 30
  private var _httpWorkerTimeoutSeconds: Int = 60
  private var _wsWorkerTimeoutSeconds: Int = 60
  private val _started = new AtomicBoolean(false)

  private var httpServer: Option[NettyServer] = None
  private var httpsServer: Option[NettyServer] = None

  override val httpRouter: HttpRouter = new HttpRouter({
    _name
  }, {
    _productionMode
  }, {
    _workerThreads
  }, {
    _httpWorkerTimeoutSeconds
  })

  val wsRouter: WSRouter = new WSRouter({
    _name
  }, {
    _productionMode
  }, {
    _workerThreads
  }, {
    _wsWorkerTimeoutSeconds
  })

  def httpRequestListeners: List[RequestListener] = httpRouter.requestListeners

  def addHttpRequestListeners(listener: RequestListener): Unit = {
    checkNotStarted()
    httpRouter.addRequestListeners(listener)
  }

  def removeHttpRequestListeners(listener: RequestListener): Unit = {
    checkNotStarted()
    httpRouter.removeRequestListeners(listener)
  }

  def wsRequestListeners: List[WSRequestListener] = wsRouter.requestListeners

  def addWSRequestListeners(listener: WSRequestListener): Unit = {
    checkNotStarted()
    wsRouter.addRequestListeners(listener)
  }

  def removeWSRequestListeners(listener: WSRequestListener): Unit = {
    checkNotStarted()
    wsRouter.removeRequestListeners(listener)
  }

  def name(value: String): Unit = {
    checkNotStarted()
    _name = value
  }

  def name: String = _name

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

  def readerIdleTimeSeconds: Int = _readerIdleTimeSeconds

  def readerIdleTimeSeconds(value: Int): Unit = {
    checkNotStarted()
    _readerIdleTimeSeconds = value
  }

  def writerIdleTimeSeconds: Int = _writerIdleTimeSeconds

  def writerIdleTimeSeconds(value: Int): Unit = {
    checkNotStarted()
    _writerIdleTimeSeconds = value
  }

  def httpWorkerTimeoutSeconds: Int = _httpWorkerTimeoutSeconds

  def httpWorkerTimeoutSeconds(value: Int): Unit = {
    checkNotStarted()
    _httpWorkerTimeoutSeconds = value
  }

  def wsWorkerTimeoutSeconds: Int = _wsWorkerTimeoutSeconds

  def wsWorkerTimeoutSeconds(value: Int): Unit = {
    checkNotStarted()
    _wsWorkerTimeoutSeconds = value
  }

  def ipAddress: String = _ipAddress

  def ipAddress(ipAddress: String): Unit = {
    checkNotStarted()
    _ipAddress = ipAddress
  }

  def started: Boolean = _started.get()

  def start(): Unit = {
    if (_started.compareAndSet(false, true)) {
      httpPort.foreach { port =>
        log.info(s"$name server is listening in http://$ipAddress:$port")
        httpServer = Some(
          new NettyServer(ipAddress, port, None, httpRouter, wsRouter, _workerThreads, _readerIdleTimeSeconds, _writerIdleTimeSeconds, _httpWorkerTimeoutSeconds)
        )
      }

      httpsPort.foreach { port =>
        log.info(s"$name server is listening in https://$ipAddress:$port")
        val sslContext = sslConfig.fold(SSLConfig.DEFAULT)(_.sslContext)
        httpsServer = Some(
          new NettyServer(ipAddress, port, Some(sslContext), httpRouter, wsRouter, _workerThreads, _readerIdleTimeSeconds, _writerIdleTimeSeconds, _httpWorkerTimeoutSeconds)
        )
      }

      httpRouter.start()
      wsRouter.start()
      httpServer.foreach(_.start())
      httpsServer.foreach(_.start())
    }
  }

  def stop(): Unit = {
    if (_started.compareAndSet(true, false)) {
      httpServer.foreach(_.stop())
      httpsServer.foreach(_.stop())
      httpRouter.stop()
      wsRouter.stop()
      httpServer = None
      httpsServer = None
    }
  }

  private def checkNotStarted(): Unit = {
    if (started) {
      throw new IllegalArgumentException("This action can only be performed before start the service")
    }
  }
}
