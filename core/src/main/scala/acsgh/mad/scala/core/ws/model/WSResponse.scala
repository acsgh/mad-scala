package acsgh.mad.scala.core.ws.model

import java.util.UUID

sealed trait WSResponse {
  val id: UUID = UUID.randomUUID()

  def close: Boolean
}

case class WSResponseClose
(
) extends WSResponse {
  val close = true
}

case class WSResponseBinary
(
  bytes: Array[Byte],
  close: Boolean = false
) extends WSResponse

case class WSResponseText
(
  text: String,
  close: Boolean = false
) extends WSResponse