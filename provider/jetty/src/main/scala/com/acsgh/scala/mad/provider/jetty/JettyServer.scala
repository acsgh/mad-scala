package com.acsgh.scala.mad.provider.jetty

import java.util.concurrent.TimeUnit

import com.acsgh.scala.mad.provider.servlet.MadServerServlet
import com.acsgh.scala.mad.router.http.HttpServer
import com.acsgh.scala.mad.router.ws.WSServer
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.eclipse.jetty.util.thread.QueuedThreadPool

case class SSLConfig
(
  keystoreFile: String,
  keystorePassword: Option[String],
  truststoreFile: Option[String],
  truststorePassword: Option[String]
)

trait JettyServer extends HttpServer with WSServer {
  protected val maxThreads: Option[Int] = None
  protected val minThreads: Option[Int] = None
  protected val threadTimeoutMillis: Option[Int] = None
  protected val webSocketIdleTimeoutMillis: Option[Int] = None

  protected val httpsPort: Option[Int] = None
  protected val sslConfig: Option[SSLConfig] = None

  private var jettyServer: Option[Server] = None

  onConfigure {
    jettyServer = Some(createServer())
  }

  onStart {
    jettyServer.foreach(_.start())
  }

  onStop {
    jettyServer.foreach(_.stop())
    jettyServer = None
  }

  private def createServer(): Server = {
    val min = minThreads.filter(_ > 0).getOrElse(8)
    val max = maxThreads.filter(_ > 0).getOrElse(200)
    val idleTimeout = threadTimeoutMillis.filter(_ > 0).getOrElse(60000)
    val server = new Server(new QueuedThreadPool(max, min, idleTimeout))

    val servlet = new MadServerServlet(this.httpRouter)

    if (wsRoutes.nonEmpty) {
      val handlers = new HandlerList
      handlers.setHandlers(
        List(
          new JettyHttpHandler(servlet, wsRouter),
          new JettyWsHandler(wsRouter, webSocketIdleTimeoutMillis).init(),
        ).toArray
      )
      server.setHandler(handlers)
    } else {
      server.setHandler(new JettyHttpHandler(servlet, wsRouter))
    }

    httpPort.foreach { port =>
      server.addConnector(getServerConnector(server, port))
    }

    httpsPort.foreach { port =>
      server.addConnector(getSecureServerConnector(server, port))
    }

    server
  }

  //  private def createWSHandler(): ServletContextHandler = {
  //    try {
  //      val webSocketServletContextHandler = new ServletContextHandler(null, "/", true, false)
  //      val webSocketUpgradeFilter: WebSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(webSocketServletContextHandler)
  //      webSocketIdleTimeoutMillis.foreach(v => webSocketUpgradeFilter.getFactory.getPolicy.setIdleTimeout(v))
  //
  //      wsRoutes.foreach{ route =>
  //        val webSocketCreator: WebSocketCreator = WebSocketCreatorFactory.create(webSocketHandlers.get(path))
  //        webSocketUpgradeFilter.addMapping(new ServletPathSpec(route.), webSocketCreator)
  //
  //      }
  //    } catch {
  //      case ex: Exception =>
  //        log.error("creation of websocket context handler failed.", ex)
  //        throw ex;
  //    }
  //  }

  private def getServerConnector(server: Server, port: Int): ServerConnector = {
    val connector = new ServerConnector(server)
    connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1))
    connector.setHost(host)
    connector.setPort(port)
    connector
  }

  private def getSecureServerConnector(server: Server, port: Int): ServerConnector = {
    sslConfig.fold[ServerConnector](throw new IllegalArgumentException("Secure port defined without any SSL config")) { config =>
      val sslContextFactory = new SslContextFactory.Server()
      sslContextFactory.setKeyStorePath(config.keystoreFile)
      config.keystorePassword.foreach(sslContextFactory.setKeyStorePassword)
      config.truststoreFile.foreach(sslContextFactory.setTrustStorePath)
      config.truststorePassword.foreach(sslContextFactory.setTrustStorePassword)

      val connector = new ServerConnector(server, sslContextFactory)
      connector.setIdleTimeout(TimeUnit.HOURS.toMillis(1))
      connector.setHost(host)
      connector.setPort(port)
      connector
    }
  }
}
