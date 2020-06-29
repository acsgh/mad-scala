package acsgh.mad.scala.server.router.http.body.reader

import java.net.URLDecoder

import acsgh.mad.scala.server.router.http.model.HttpRequestContext

package object urlFormEncoded {

  case class UrlFormEncodedBody
  (
    params: Map[String, List[String]]
  )

  implicit object UrlFormEncodedBodyReader extends HttpBodyReader[UrlFormEncodedBody] {

    override val contentTypes: Set[String] = Set("application/x-www-form-urlencoded")

    override val strictContentTypes: Boolean = true

    override def read(body: Array[Byte])(implicit context: HttpRequestContext): UrlFormEncodedBody = {
      val rawContent = new String(body, "UTF-8")
      val params = rawContent.split("&", -1)
        .flatMap { param =>
          val paramParts = param.split("=", -1)

          if (paramParts.length > 1) {
            val key = URLDecoder.decode(paramParts(0), "UTF-8")
            val value = URLDecoder.decode(paramParts(1), "UTF-8")
            Option(key -> value)
          } else {
            None
          }
        }.groupBy { case (key, _) => key }
        .view
        .mapValues(_.map { case (_, value) => value }.toList)
        .toMap

      UrlFormEncodedBody(params)

    }
  }

}
