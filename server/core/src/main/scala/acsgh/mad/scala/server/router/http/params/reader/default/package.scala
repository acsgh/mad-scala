package acsgh.mad.scala.server.router.http.params.reader

import acsgh.mad.scala.server.router.http.body.reader.multipart.{MultipartFile, MultipartPart}

package object default {

  implicit object StringReader extends HttpParamReader[String, String] {
    override def read(input: String): String = input
  }

  implicit object LongReader extends HttpParamReader[String, Long] {
    override def read(input: String): Long = input.toLong
  }

  implicit object MultipartFileReader extends HttpParamReader[MultipartPart, MultipartFile] {
    override def read(input: MultipartPart): MultipartFile = MultipartFile(
      contentType = input.contentType.getOrElse("application/octet-stream"),
      filename = input.filename,
      content = input.content
    )
  }

}
