package acsgh.mad.scala.server.router.http.body.reader

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.server.router.http.directives.HttpRequestHeaderDirectives._
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import acsgh.mad.scala.server.router.http.params.reader.default._
import org.apache.commons.fileupload.MultipartStream

package object multipart {

  implicit object MultipartBodyReader extends HttpBodyReader[Multipart] {

    final val EndLine = "\r\n"

    final private val DefaultBuffSize = 4096

    override val contentTypes: Set[String] = Set("multipart/form-data")

    override val strictContentTypes: Boolean = true

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): Multipart = {
      val separator = "; boundary="

      requestHeader("Content-Type") { contentType =>


        val boundary = contentType.substring(contentType.lastIndexOf(separator) + separator.length)

        try {
          val multipartStream = new MultipartStream(new ByteArrayInputStream(body), boundary.getBytes(StandardCharsets.UTF_8), DefaultBuffSize, null)
          var nextPart = multipartStream.skipPreamble

          var parts = List.empty[MultipartPart]

          while (nextPart) {

            val headers = multipartStream.readHeaders.split(EndLine)
              .map { h =>
                h.substring(0, h.indexOf(":")) -> h.substring(h.indexOf(":") + 2)
              }
              .toMap

            val contentDisposition = headers.getOrElse("Content-Disposition", {
              throw new BadRequestException("The multipart body is not properly formatted")
            })

            val output = new ByteArrayOutputStream()
            multipartStream.readBodyData(output)

            val part = MultipartPart(
              name = extractAttr(contentDisposition, "name").getOrElse {
                throw new BadRequestException("The multipart body is not properly formatted")
              },
              contentType = headers.get("Content-Type"),
              filename = extractAttr(contentDisposition, "filename").filter(_.nonEmpty),
              content = output.toByteArray
            )
            output.close()

            parts = parts ++ List(part)

            nextPart = multipartStream.readBoundary
          }

          Multipart(
            parts = parts
          )
        } catch {
          case e: MultipartStream.MalformedStreamException =>
            throw new BadRequestException("The multipart body is not properly formatted", e)
        }
      }
    }

    private def extractAttr(src: String, attrName: String): Option[String] = {
      val pattern = s"""\\s$attrName="([^"]*)"""".r
      val iterator = pattern.findAllIn(src)
      Option(iterator).filter(_ => pattern.findFirstIn(src).isDefined).map(_.group(1))
    }
  }

}
