package acsgh.mad.scala.core.http.model

import java.net.URI
import java.util.UUID

import acsgh.mad.scala.core.URLSupport

case class HttpRequest
(
  method: RequestMethod,
  uri: URI,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte],
  remoteAddress: Option[String] = None
) extends URLSupport {

  val id: UUID = UUID.randomUUID()

  val starTime: Long = System.currentTimeMillis()

  val path: String = uri.getPath

  val fullUri: String = uri.toString

  val queryParams: Map[String, List[String]] = extractQueryParam(uri)

  val cookieParams: Map[String, List[String]] = headers.getOrElse("Cookie", List()).flatMap(_.split(";")).map(extractCookie).groupBy(_._1).view.mapValues(_.map(_._2).toList).toMap

  private[scala] def validContentType(contentTypes: Set[String], contentType: String) = contentTypes.isEmpty || contentTypes.exists(t => t.equalsIgnoreCase(contentType))
}

object HttpRequestBuilder {
  def from(request: HttpRequest): HttpRequestBuilder = HttpRequestBuilder(request.method, request.uri, request.protocolVersion)
}

case class HttpRequestBuilder
(
  private var requestMethod: RequestMethod,
  private var requestUri: URI,
  private var protocolVersion: ProtocolVersion,
  private var httpHeaders: Map[String, List[String]] = Map(),
  private var bodyBytes: Array[Byte] = Array[Byte]()
) {
  def version(input: ProtocolVersion): HttpRequestBuilder = {
    protocolVersion = input
    this
  }

  def method: RequestMethod = requestMethod

  def method(input: RequestMethod): HttpRequestBuilder = {
    requestMethod = input
    this
  }

  def uri: URI = requestUri

  def uri(input: URI): HttpRequestBuilder = {
    requestUri = input
    this
  }

  def hasHeader(name: String): Boolean = httpHeaders.contains(name)

  def headers(input: Map[String, List[String]]): HttpRequestBuilder = {
    httpHeaders = input
    this
  }

  def header(key: String, value: String): HttpRequestBuilder = {
    val previousHeaders: List[String] = httpHeaders.getOrElse(key, List())
    val currentHeaders: List[String] = previousHeaders ++ List(value)
    httpHeaders = httpHeaders + (key -> currentHeaders)
    this
  }

  def body(input: Array[Byte]): HttpRequestBuilder = {
    bodyBytes = input
    this
  }

  def build: HttpRequest = HttpRequest(requestMethod, requestUri, protocolVersion, httpHeaders, bodyBytes)
}

