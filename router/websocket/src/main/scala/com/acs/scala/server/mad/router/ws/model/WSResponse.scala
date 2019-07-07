package com.acs.scala.server.mad.router.ws.model

import java.net.URI

sealed trait WSResponse {
  val id: String
  val remoteAddress: String
  val uri: URI
  val close: Boolean
}

case class WSResponseConnect
(
  id: String,
  remoteAddress: String,
  uri: URI,
  close: Boolean
) extends WSResponse

case class WSResponseDisconnect
(
  id: String,
  remoteAddress: String,
  uri: URI,
  close: Boolean
) extends WSResponse

case class WSResponseBinary
(
  id: String,
  remoteAddress: String,
  uri: URI,
  close: Boolean,
  bytes: Array[Byte]
) extends WSResponse

case class WSResponseText
(
  id: String,
  remoteAddress: String,
  uri: URI,
  close: Boolean,
  text: String
) extends WSResponse

object WSResponseBuilder {
  def apply(request: WSRequest): WSResponseBuilder = WSResponseBuilder(request.id, request.remoteAddress, request.uri)
}

case class WSResponseBuilder
(
  private var id: String,
  private var remoteAddress: String,
  private var uri: URI,
  private var close: Boolean = false,
  private var text: Option[String] = None,
  private var bytes: Option[Array[Byte]] = None
) {
  def close(input: Boolean): WSResponseBuilder = {
    close = input
    this
  }

  def text(input: String): WSResponseBuilder = {
    text = Some(input)
    bytes = None
    this
  }

  def bytes(input: Array[Byte]): WSResponseBuilder = {
    bytes = Some(input)
    text = None
    this
  }

  def build: WSResponse = {
    if (text.isDefined) {
      WSResponseText(id, remoteAddress, uri, close, text.get)
    } else if (bytes.isDefined) {
      WSResponseBinary(id, remoteAddress, uri, close, bytes.get)
    } else {
      throw new IllegalArgumentException("You have not fill test or bytes")
    }
  }
}