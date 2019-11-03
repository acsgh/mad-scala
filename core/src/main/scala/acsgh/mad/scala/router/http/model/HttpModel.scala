package acsgh.mad.scala.router.http.model

import java.net.URI
import java.util.UUID

import acsgh.mad.scala.URLSupport

case class Request
(
  method: RequestMethod,
  remoteAddress: String,
  uri: URI,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends URLSupport {

  val id: UUID = UUID.randomUUID()

  val starTime: Long = System.currentTimeMillis()

  val path: String = uri.getPath

  val fullUri: String = uri.toString

  val queryParams: Map[String, List[String]] = extractQueryParam(uri)

  val cookieParams: Map[String, List[String]] = headers.getOrElse("Cookie", List()).flatMap(_.split(";")).map(extractCookie).groupBy(_._1).toMap.mapValues(_.map(_._2).toList).toMap

  private[router] def validContentType(contentTypes: Set[String], contentType: String) = contentTypes.isEmpty || contentTypes.exists(t => t.equalsIgnoreCase(contentType))
}

case class Response
(
  responseStatus: ResponseStatus,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
)

object ResponseBuilder {
  def apply(request: Request) = new ResponseBuilder(request.protocolVersion)
}

case class ResponseBuilder
(
  private var protocolVersion: ProtocolVersion,
  private var responseStatus: ResponseStatus = ResponseStatus.OK,
  private var httpHeaders: Map[String, List[String]] = Map(),
  private var bodyBytes: Array[Byte] = Array[Byte]()
) {
  def version(input: ProtocolVersion): ResponseBuilder = {
    protocolVersion = input
    this
  }

  def status: ResponseStatus = responseStatus

  def status(input: ResponseStatus): ResponseBuilder = {
    responseStatus = input
    this
  }

  def hasHeader(name: String): Boolean = httpHeaders.contains(name)

  def headers(input: Map[String, List[String]]): ResponseBuilder = {
    httpHeaders = input
    this
  }

  def header(key: String, value: String): ResponseBuilder = {
    val previousHeaders: List[String] = httpHeaders.getOrElse(key, List())
    val currentHeaders: List[String] = previousHeaders ++ List(value)
    httpHeaders = httpHeaders + (key -> currentHeaders)
    this
  }

  def body(input: Array[Byte]): ResponseBuilder = {
    bodyBytes = input
    this
  }

  def build = Response(responseStatus, protocolVersion, httpHeaders, bodyBytes)
}

