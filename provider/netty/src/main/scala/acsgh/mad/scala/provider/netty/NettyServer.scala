package acsgh.mad.scala.provider.netty

import java.io.File

import acsgh.mad.scala.router.http.HttpServer
import acsgh.mad.scala.router.ws.WSServer
import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.handler.ssl.{SslContext, SslContextBuilder}

case class SSLConfig
(
  keyCertChainFile: File,
  keyFile: File,
  keyPassword: Option[String]
)

trait NettyServer extends HttpServer with WSServer {
  protected val maxThreads: Option[Int] = None
  protected val minThreads: Option[Int] = None
  protected val threadTimeoutMillis: Option[Int] = None

  protected val httpsPort: Option[Int] = None
  protected val sslConfig: Option[SSLConfig] = None

  private var httpServer: Option[NettyServerChannel] = None
  private var httpsServer: Option[NettyServerChannel] = None

  onConfigure {
    httpPort.foreach { port =>
      httpServer = Some(
        new NettyServerChannel(host, port, None, httpRouter, wsRouter)
      )
    }

    httpsPort.foreach { port =>
      httpsServer = Some(
        new NettyServerChannel(host, port, Some(sslContext), httpRouter, wsRouter)
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

  private def sslContext: SslContext = {
    sslConfig.fold(defaultSslContext) { config =>
      SslContextBuilder.forServer(config.keyCertChainFile, config.keyFile, config.keyPassword.getOrElse(null)).build
    }
  }

  private val defaultSslContext = {
    val ssc = new SelfSignedCertificate
    SslContextBuilder.forServer(ssc.certificate, ssc.privateKey).build
  }
}
