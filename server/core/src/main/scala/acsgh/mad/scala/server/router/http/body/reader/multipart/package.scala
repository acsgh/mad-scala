package acsgh.mad.scala.server.router.http.body.reader

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.server.router.http.directives.HttpRequestHeaderDirectives._
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

package object multipart {

  case class Multipart
  (
    parts: List[MultipartPart]
  )

  case class MultipartPart
  (
    name: String,
    contentType: Option[String],
    filename: Option[String],
    content: String
  )

  implicit object MultipartBodyReader extends HttpBodyReader[Multipart] {

    final val EndLine = "\r\n"

    override val contentTypes: Set[String] = Set("multipart/form-data")

    override val strictContentTypes: Boolean = true

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): Multipart = {
      val separator = "; boundary="

      requestHeader("Content-Type") { contentType =>


        val boundary = contentType.substring(contentType.lastIndexOf(separator) + separator.length)
        val rawContent = new String(body, "UTF-8")

        val endBoundary = s"$boundary--$EndLine"

        if (rawContent.endsWith(endBoundary)) {
          val parts = rawContent.substring(0, rawContent.length - endBoundary.length)
            .split(s"--$boundary$EndLine", -1)
            .filter(_.nonEmpty)
            .map { entryContent =>
              val entryPart = entryContent.split(s"$EndLine$EndLine", -1)

              val headers = entryPart(0).split(EndLine)
                .map { h =>
                  (h.substring(0, h.indexOf(":")) -> h.substring(h.indexOf(":") + 2))
                }
                .toMap


              val content = entryPart(1)
                .substring(0, entryPart(1).length - EndLine.size)

              val contentDisposition = headers.getOrElse("Content-Disposition", {
                throw new BadRequestException("The multipart body is not properly formatted")
              })

              MultipartPart(
                name = extractAttr(contentDisposition, "name").getOrElse {
                  throw new BadRequestException("The multipart body is not properly formatted")
                },
                contentType = headers.get("Content-Type"),
                filename = extractAttr(contentDisposition, "filename").filter(_.nonEmpty),
                content = content
              )
            }
            .toList

          Multipart(
            parts = parts
          )

        } else {
          throw new BadRequestException("The multipart body does not end properly")
        }

        //      val params = rawContent.split("&", -1)
        //        .flatMap { param =>
        //          val paramParts = param.split("=", -1)
        //
        //          if (paramParts.length > 1) {
        //            val key = URLDecoder.decode(paramParts(0), "UTF-8")
        //            val value = URLDecoder.decode(paramParts(1), "UTF-8")
        //            Option(key -> value)
        //          } else {
        //            None
        //          }
        //        }.groupBy { case (key, _) => key }
        //        .mapValues(_.map { case (_, value) => value }.toList)
        //        .toMap
        //
        //      Multipart(params)

      }
    }

    private def extractAttr(src: String, attrName: String): Option[String] = {
      val pattern = s"""$attrName="(\\w*)"""".r
      val iterator = pattern.findAllIn(src)
      Option(iterator).filter(_ => pattern.findFirstIn(src).isDefined).map(_.group(1))
    }
  }

}
