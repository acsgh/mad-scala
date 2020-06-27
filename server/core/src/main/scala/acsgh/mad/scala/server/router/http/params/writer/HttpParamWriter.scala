package acsgh.mad.scala.server.router.http.params.writer

trait HttpParamWriter[I, O] {
  def write(input: I): O
}