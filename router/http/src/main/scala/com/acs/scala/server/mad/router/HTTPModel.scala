package com.acs.scala.server.mad.router

import java.nio.charset.StandardCharsets

import com.acs.scala.server.mad.router.constant.{ProtocolVersion, RedirectStatus, RequestMethod, ResponseStatus}
import com.acs.scala.server.mad.router.exception.UnexpectedContentTypeException

trait BodyReader[T] {
  val contentTypes: Set[String] = Set()

  def read(body: Array[Byte]): T
}

trait BodyWriter[T] {
  val contentType: String = "text/html"

  def write(body: T): Array[Byte]
}

trait HTTPModel {
  val protocolVersion: ProtocolVersion
  val headers: Map[String, List[String]]
}

case class HTTPRequest
(
  method: RequestMethod,
  remoteAddress: String,
  private[router] val address: HTTPAddress,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends HTTPModel {

  val uri: String = address.uri.getPath

  val fullUri: String = address.uri.toString

  val pathParams: HTTPParams = address.pathParams

  val queryParams: HTTPParams = address.queryParams

  def entity[T](implicit reader: BodyReader[T]): T = {
    val contentType = headers("Content-Type").head

    if (!validContentType(reader.contentTypes, contentType)) {
      throw new UnexpectedContentTypeException(contentType)
    }

    reader.read(bodyBytes)
  }

  private[router] def ofUri(uri: String) = copy(address = HTTPAddress.build(uri))

  private[router] def ofRoute(httpRoute: HTTPRoute[_]) = copy(address = address.ofRoute(httpRoute.uri))

  private def validContentType(contentTypes: Set[String], contentType: String) = contentTypes.isEmpty || contentTypes.exists(t => t.equalsIgnoreCase(contentType))
}

case class HTTPResponse
(
  responseStatus: ResponseStatus,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends HTTPModel {
  def header(key: String, value: String): HTTPResponse = {
    copy(headers = headers + (key -> headers.getOrElse(key, List())))
  }
}

object HTTPResponseBuilder {
  def apply(router: HTTPRouter, request: HTTPRequest) = new HTTPResponseBuilder(router, request, request.protocolVersion)
}

case class HTTPResponseBuilder
(
  private var httpRouter: HTTPRouter,
  private var httpRequest: HTTPRequest,
  private var protocolVersion: ProtocolVersion,
  private var responseStatus: ResponseStatus = ResponseStatus.OK,
  private var httpHeaders: Map[String, List[String]] = Map(),
  private var bodyBytes: Array[Byte] = Array[Byte]()
) {
  def version(input: ProtocolVersion): HTTPResponseBuilder = copy(protocolVersion = input)

  def status(input: ResponseStatus): HTTPResponseBuilder = copy(responseStatus = input)

  def headers(input: Map[String, List[String]]): HTTPResponseBuilder = copy(httpHeaders = input)

  def header(key: String, value: String): HTTPResponseBuilder = copy(httpHeaders = httpHeaders + (key -> httpHeaders.getOrElse(key, List())))

  def body(input: Array[Byte]): HTTPResponseBuilder = copy(bodyBytes = input)

  def body[T](input: T)(implicit writer: BodyWriter[T]): HTTPResponseBuilder = {
    val resultWithHeader = if(httpHeaders.contains("Content-Type")) this else header("Content-Type", writer.contentType)
    resultWithHeader.body(writer.write(input))
  }

  def build = HTTPResponse(responseStatus, protocolVersion, httpHeaders, bodyBytes)

  def redirect(url: String): HTTPResponse = redirect(url, RedirectStatus.FOUND)

  def redirect(url: String, redirectStatus: RedirectStatus): HTTPResponse =
    httpRouter.getErrorResponse(httpRequest, header("Location", url), redirectStatus.status)

  def error(errorCode: ResponseStatus): HTTPResponse = httpRouter.getErrorResponse(httpRequest, this, errorCode)

  def serve(url: String): HTTPResponse = httpRouter.process(httpRequest.ofUri(url))

}

