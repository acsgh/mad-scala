package acsgh.mad.scala

import java.io.File

import io.netty.handler.ssl.util.SelfSignedCertificate
import io.netty.handler.ssl.{SslContext, SslContextBuilder}

object SSLConfig {
  private[scala] val DEFAULT = {
    val ssc = new SelfSignedCertificate
    SslContextBuilder.forServer(ssc.certificate, ssc.privateKey).build
  }
}

case class SSLConfig
(
  keyCertChainFile: File,
  keyFile: File,
  keyPassword: Option[String]
) {
  val sslContext: SslContext = SslContextBuilder.forServer(keyCertChainFile, keyFile, keyPassword.orNull).build
}
