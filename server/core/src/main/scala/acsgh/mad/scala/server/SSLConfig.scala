package acsgh.mad.scala.server

import java.io.File

case class SSLConfig
(
  keyCertChainFile: File,
  keyCertChainPassword: Option[String],
  keyFile: File,
  keyPassword: Option[String]
)
