package com.acs.scala.server.mad.router

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
  def apply(router: HttpRouter, request: Request) = new ResponseBuilder(router, request, request.protocolVersion)
}

case class ResponseBuilder
(
  private var httpRouter: HttpRouter,
  private var httpRequest: Request,
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

  def body[T](input: T)(implicit writer: BodyWriter[T]): ResponseBuilder = {
    if (!httpHeaders.contains("Content-Type")) {
      header("Content-Type", writer.contentType)
    }
    body(writer.write(input))
  }

  def build = Response(responseStatus, protocolVersion, httpHeaders, bodyBytes)

  def redirect(url: String)(implicit requestContext: RequestContext): Response = redirect(url, RedirectStatus.FOUND)

  def redirect(url: String, redirectStatus: RedirectStatus)(implicit requestContext: RequestContext): Response = {
    requestContext.responseBuilder.header("Location", url)
    httpRouter.getErrorResponse(requestContext, redirectStatus.status)
  }

  def error(errorCode: ResponseStatus)(implicit requestContext: RequestContext): Response = httpRouter.getErrorResponse(requestContext, errorCode)

  def serve(url: String): Response = httpRouter.process(httpRequest.ofUri(url))

}

