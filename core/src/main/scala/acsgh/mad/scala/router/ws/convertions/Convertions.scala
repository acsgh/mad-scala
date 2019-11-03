package acsgh.mad.scala.router.ws.convertions

import acsgh.mad.scala.router.ws.model.WSRequestContext

trait BodyReader[T] {
  def read(body: Array[Byte])(implicit context: WSRequestContext): T

  def read(body: String)(implicit context: WSRequestContext): T
}

trait BodyWriter[T] {
  def writeBytes(body: T)(implicit context: WSRequestContext): Array[Byte]

  def writeText(body: T)(implicit context: WSRequestContext): String
}