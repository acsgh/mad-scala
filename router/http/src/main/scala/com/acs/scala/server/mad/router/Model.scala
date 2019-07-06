package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.{ProtocolVersion, RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.convertions.BodyReader
import com.acs.scala.server.mad.router.exception.UnexpectedContentTypeException

trait Model {
  val protocolVersion: ProtocolVersion
  val headers: Map[String, List[String]]
}

case class Request
(
  method: RequestMethod,
  remoteAddress: String,
  private[router] val address: Address,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends Model {

  val uri: String = address.uri.getPath

  val fullUri: String = address.uri.toString

  val pathParams: Params = address.pathParams

  val queryParams: Params = address.queryParams

  def entity[T](implicit reader: BodyReader[T]): T = {
    val contentType = headers("Content-Type").head

    if (!validContentType(reader.contentTypes, contentType)) {
      throw UnexpectedContentTypeException(contentType)
    }

    reader.read(bodyBytes)
  }

  private[router] def ofUri(uri: String) = copy(address = Address.build(uri))

  private[router] def ofRoute(httpRoute: Route[_]) = copy(address = address.ofRoute(httpRoute.uri))

  private def validContentType(contentTypes: Set[String], contentType: String) = contentTypes.isEmpty || contentTypes.exists(t => t.equalsIgnoreCase(contentType))
}

case class Response
(
  responseStatus: ResponseStatus,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends Model

object ResponseBuilder {
  def apply(router: HttpRouter, request: Request) = new ResponseBuilder(request.protocolVersion)
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

