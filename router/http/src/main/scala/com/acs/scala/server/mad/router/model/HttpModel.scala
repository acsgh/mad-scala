package com.acs.scala.server.mad.router.model

import java.net.URI

import com.acs.scala.server.mad.URLSupport
import com.acs.scala.server.mad.router.HttpRouter

case class Request
(
  method: RequestMethod,
  remoteAddress: String,
  uri: URI,
  protocolVersion: ProtocolVersion,
  headers: Map[String, List[String]],
  bodyBytes: Array[Byte]
) extends URLSupport {

  val path: String = uri.getPath

  val fullUri: String = uri.toString

  val queryParams: Map[String, List[String]] = extractQueryParam(uri)

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

