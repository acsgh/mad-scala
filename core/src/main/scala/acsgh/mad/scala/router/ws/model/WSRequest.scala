package acsgh.mad.scala.router.ws.model

import java.net.URI
import java.util.UUID

sealed trait WSRequest {
  val remoteAddress: String
  val uri: URI
  val id: UUID = UUID.randomUUID()
  val starTime: Long = System.currentTimeMillis()
}

case class WSRequestConnect
(
  remoteAddress: String,
  uri: URI,
) extends WSRequest

case class WSRequestDisconnect
(
  remoteAddress: String,
  uri: URI,
) extends WSRequest

case class WSRequestBinary
(
  remoteAddress: String,
  uri: URI,
  bytes: Array[Byte]
) extends WSRequest

case class WSRequestText
(
  remoteAddress: String,
  uri: URI,
  text: String
) extends WSRequest