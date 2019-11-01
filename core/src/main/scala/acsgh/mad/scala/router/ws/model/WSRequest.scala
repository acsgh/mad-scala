package acsgh.mad.scala.router.ws.model

import java.net.URI

sealed trait WSRequest {
  val id: String
  val remoteAddress: String
  val uri: URI
}

case class WSRequestConnect
(
  id: String,
  remoteAddress: String,
  uri: URI,
) extends WSRequest

case class WSRequestDisconnect
(
  id: String,
  remoteAddress: String,
  uri: URI,
) extends WSRequest

case class WSRequestBinary
(
  id: String,
  remoteAddress: String,
  uri: URI,
  bytes: Array[Byte]
) extends WSRequest

case class WSRequestText
(
  id: String,
  remoteAddress: String,
  uri: URI,
  text: String
) extends WSRequest