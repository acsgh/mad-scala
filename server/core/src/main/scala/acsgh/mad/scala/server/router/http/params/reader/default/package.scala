package acsgh.mad.scala.server.router.http.params.reader

package object default {

  implicit object StringReader extends HttpParamReader[String, String] {
    override def read(input: String): String = input
  }

  implicit object LongReader extends HttpParamReader[String, Long] {
    override def read(input: String): Long = input.toLong
  }

}
