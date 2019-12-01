package acsgh.mad.scala.core.http.model

case class HttpResponse
(
  responseStatus: ResponseStatus,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
)

object HttpResponseBuilder {
  def from(request: HttpRequest): HttpResponseBuilder = HttpResponseBuilder(request.protocolVersion)
}

case class HttpResponseBuilder
(
  private var protocolVersion: ProtocolVersion,
  private var responseStatus: ResponseStatus = ResponseStatus.OK,
  private var httpHeaders: Map[String, List[String]] = Map(),
  private var bodyBytes: Array[Byte] = Array[Byte]()
) {
  def version(input: ProtocolVersion): HttpResponseBuilder = {
    protocolVersion = input
    this
  }

  def status: ResponseStatus = responseStatus

  def status(input: ResponseStatus): HttpResponseBuilder = {
    responseStatus = input
    this
  }

  def hasHeader(name: String): Boolean = httpHeaders.contains(name)

  def headers(input: Map[String, List[String]]): HttpResponseBuilder = {
    httpHeaders = input
    this
  }

  def header(key: String, value: String): HttpResponseBuilder = {
    val previousHeaders: List[String] = httpHeaders.getOrElse(key, List())
    val currentHeaders: List[String] = previousHeaders ++ List(value)
    httpHeaders = httpHeaders + (key -> currentHeaders)
    this
  }

  def body(input: Array[Byte]): HttpResponseBuilder = {
    bodyBytes = input
    this
  }

  def build: HttpResponse = HttpResponse(responseStatus, protocolVersion, httpHeaders, bodyBytes)
}

