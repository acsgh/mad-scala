package acsgh.mad.scala.server

import java.io.File

case class SSLConfig
(
  keyCertChainFile: File,
  keyFile: File,
  keyPassword: Option[String]
)
