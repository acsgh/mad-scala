package acsgh.mad.scala.server.router.http.params.reader

trait HttpParamReader[I, O] {
  def read(input: I): O
}
