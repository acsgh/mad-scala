package acsgh.mad.scala

import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.provider.NettyServer
import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.ws.WSRouter
import com.acsgh.common.scala.log.LogSupport

case class Server
(
  name: String,
  productionMode: Boolean,
  ipAddress: String,
  httpPort: Option[Int],
  httpsPort: Option[Int],
  sslConfig: Option[SSLConfig],
  readerIdleTimeSeconds: Int,
  writerIdleTimeSeconds: Int,
  httpRouter: HttpRouter,
  wsRouter: WSRouter

) extends LogSupport {

  private val _started = new AtomicBoolean(false)

  private var httpServer: Option[NettyServer] = None
  private var httpsServer: Option[NettyServer] = None

  def started: Boolean = _started.get()

  def start(): Unit = {
    val workerThreads = httpRouter.workerThreads + wsRouter.workerThreads

    if (_started.compareAndSet(false, true)) {
      httpPort.foreach { port =>
        log.info(s"$name server is listening in http://$ipAddress:$port")
        httpServer = Some(
          new NettyServer(ipAddress, port, None, httpRouter, wsRouter, workerThreads, readerIdleTimeSeconds, writerIdleTimeSeconds)
        )
      }

      httpsPort.foreach { port =>
        log.info(s"$name server is listening in https://$ipAddress:$port")
        val sslContext = sslConfig.fold(SSLConfig.DEFAULT)(_.sslContext)
        httpsServer = Some(
          new NettyServer(ipAddress, port, Some(sslContext), httpRouter, wsRouter, workerThreads, readerIdleTimeSeconds, writerIdleTimeSeconds)
        )
      }

      httpServer.foreach(_.start())
      httpsServer.foreach(_.start())
    }
  }

  def stop(): Unit = {
    if (_started.compareAndSet(true, false)) {
      httpServer.foreach(_.stop())
      httpsServer.foreach(_.stop())
      httpRouter.close()
      wsRouter.close()
      httpServer = None
      httpsServer = None
    }
  }
}
