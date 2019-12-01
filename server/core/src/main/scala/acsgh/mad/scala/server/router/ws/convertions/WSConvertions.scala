package acsgh.mad.scala.server.router.ws.convertions

import acsgh.mad.scala.server.router.ws.model.WSRequestContext

trait WSBodyReader[T] {
  def read(body: Array[Byte])(implicit context: WSRequestContext): T

  def read(body: String)(implicit context: WSRequestContext): T
}

trait WSBodyWriter[T] {
  def writeBytes(body: T)(implicit context: WSRequestContext): Array[Byte]

  def writeText(body: T)(implicit context: WSRequestContext): String
}