package acsgh.mad.scala.server.provider.jetty

import acsgh.mad.scala.server.provider.jetty.internal.NettyServer
import acsgh.mad.scala.server.provider.jetty.internal.{NettyServer => NettyServerInt}
import acsgh.mad.scala.server.router.http.HttpRouter
import acsgh.mad.scala.server.router.ws.WSRouter
import acsgh.mad.scala.server.{SSLConfig, Server}
import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.handler.ssl.{SslContext, SslContextBuilder}

object JettyServer {
  private[scala] val DEFAULT_SSL_CONTEXT = {
    val ssc = new SelfSignedCertificate()
    SslContextBuilder.forServer(ssc.certificate, ssc.privateKey).build
  }

  implicit class NettySSLConfig(config: SSLConfig) {
    val sslContext: SslContext = SslContextBuilder.forServer(config.keyCertChainFile, config.keyFile, config.keyPassword.orNull).build
  }

}

import JettyServer._

case class JettyServer
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
) extends Server {

  private var httpServer: Option[NettyServer] = None
  private var httpsServer: Option[NettyServer] = None

  override protected def doStart(): Unit = {
    val workerThreads = httpRouter.workerThreads + wsRouter.workerThreads

    httpPort.foreach { port =>
      log.info(s"$name server is listening in http://$ipAddress:$port")
      httpServer = Some(
        new NettyServer(ipAddress, port, None, httpRouter, wsRouter, workerThreads, readerIdleTimeSeconds, writerIdleTimeSeconds)
      )
    }

    httpsPort.foreach { port =>
      log.info(s"$name server is listening in https://$ipAddress:$port")
      val sslContext = sslConfig.fold(DEFAULT_SSL_CONTEXT)(_.sslContext)
      httpsServer = Some(
        new NettyServer(ipAddress, port, Some(sslContext), httpRouter, wsRouter, workerThreads, readerIdleTimeSeconds, writerIdleTimeSeconds)
      )
    }

    httpServer.foreach(_.start())
    httpsServer.foreach(_.start())
  }

  override protected def doStop(): Unit = {
    httpServer.foreach(_.stop())
    httpsServer.foreach(_.stop())
    httpServer = None
    httpsServer = None
  }
}
