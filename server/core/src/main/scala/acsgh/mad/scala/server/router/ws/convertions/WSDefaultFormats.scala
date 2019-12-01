package acsgh.mad.scala.server.router.ws.convertions

import acsgh.mad.scala.server.router.ws.model.WSRequestContext

trait WSDefaultFormats {

  implicit object DefaultBytesBodyReader extends WSBodyReader[Array[Byte]] {
    override def read(body: Array[Byte])(implicit context: WSRequestContext): Array[Byte] = body

    override def read(body: String)(implicit context: WSRequestContext): Array[Byte] = body.getBytes("UTF-8")
  }

  implicit object DefaultStringBodyReader extends WSBodyReader[String] {
    override def read(body: String)(implicit context: WSRequestContext): String = body

    override def read(body: Array[Byte])(implicit context: WSRequestContext): String = new String(body, "UTF-8")
  }

  implicit object DefaultBytesBodyWriter extends WSBodyWriter[Array[Byte]] {
    override def writeBytes(body: Array[Byte])(implicit context: WSRequestContext): Array[Byte] = body

    override def writeText(body: Array[Byte])(implicit context: WSRequestContext): String = new String(body, "UTF-8")
  }

  implicit object DefaultStringBodyWriter extends WSBodyWriter[String] {
    override def writeBytes(body: String)(implicit context: WSRequestContext): Array[Byte] = body.getBytes("UTF-8")

    override def writeText(body: String)(implicit context: WSRequestContext): String = body
  }

}
