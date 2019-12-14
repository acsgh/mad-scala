package acsgh.mad.scala.server.provider.jetty

import java.util.concurrent.TimeUnit

import acsgh.mad.scala.server.provider.jetty.internal.{JettyHttpHandler, JettyWsHandler}
import acsgh.mad.scala.server.provider.servlet.MadServerServlet
import acsgh.mad.scala.server.router.http.HttpRouter
import acsgh.mad.scala.server.router.ws.WSRouter
import acsgh.mad.scala.server.{SSLConfig, Server}
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.{ServerConnector, Server => ServerInt}
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.util.thread.QueuedThreadPool

object JettyServer {
//  private[scala] val DEFAULT_SSL_CONTEXT = {
  //    val ssc = new SelfSignedCertificate()
  //    SslContextBuilder.forServer(ssc.certificate, ssc.privateKey).build
  //  }
  //
  //  implicit class NettySSLConfig(config: SSLConfig) {
  //    val sslContext: SslContext = SslContextBuilder.forServer(config.keyCertChainFile, config.keyFile, config.keyPassword.orNull).build
  //  }

}

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

  private var httpServer: Option[ServerInt] = None

  override protected def doStart(): Unit = {
    httpServer = Some(createServer())
    httpServer.foreach(_.start())
  }

  override protected def doStop(): Unit = {
    httpServer.foreach(_.stop())
    httpServer = None
  }

  private def createServer(): ServerInt = {
    val workerThreads = httpRouter.workerThreads + wsRouter.workerThreads

    val server = new ServerInt(new QueuedThreadPool(workerThreads))

    val servlet = new MadServerServlet(this.httpRouter)

    if (wsRouter.wsRoutes.nonEmpty) {
      val handlers = new HandlerList
      handlers.setHandlers(
        List(
          new JettyHttpHandler(servlet, wsRouter),
          JettyWsHandler.build(wsRouter),
        ).toArray
      )
      server.setHandler(handlers)
    } else {
      server.setHandler(new JettyHttpHandler(servlet, wsRouter))
    }

    httpPort.foreach { port =>
      log.info(s"$name server is listening in http://$ipAddress:$port")
      server.addConnector(getServerConnector(server, port))
    }

    httpsPort.foreach { port =>
      log.info(s"$name server is listening in https://$ipAddress:$port")
      server.addConnector(getSecureServerConnector(server, port))
    }

    server
  }

  private def getServerConnector(server: ServerInt, port: Int): ServerConnector = {
    val connector = new ServerConnector(server)
    connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1))
    connector.setHost(ipAddress)
    connector.setPort(port)
    connector
  }

  private def getSecureServerConnector(server: ServerInt, port: Int): ServerConnector = {
    sslConfig.fold[ServerConnector](throw new IllegalArgumentException("Secure port defined without any SSL config")) { config =>
      val sslContextFactory = new SslContextFactory.Server()

      sslContextFactory.setTrustStorePath(config.keyCertChainFile.getAbsolutePath)
      config.keyCertChainPassword.foreach(sslContextFactory.setTrustStorePassword)

      sslContextFactory.setKeyStorePath(config.keyFile.getAbsolutePath)
      config.keyPassword.foreach(sslContextFactory.setKeyStorePassword)

      val connector = new ServerConnector(server, sslContextFactory)
      connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1))
      connector.setHost(ipAddress)
      connector.setPort(port)
      connector
    }
  }
}
