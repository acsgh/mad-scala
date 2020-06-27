package acsgh.mad.scala.server.router.http.params.writer

package object default {

  implicit object StringWriter extends HttpParamWriter[String, String] {
    override def write(input: String): String = input
  }

  implicit object LongWriter extends HttpParamWriter[Long, String] {
    override def write(input: Long): String = input.toString
  }

}
