package acsgh.mad.scala.core.ws.model

import java.util.UUID

sealed trait WSResponse {
  val id: UUID = UUID.randomUUID()
}

case class WSResponseClose
(
) extends WSResponse

case class WSResponseBinary
(
  bytes: Array[Byte]
) extends WSResponse

case class WSResponseText
(
  text: String
) extends WSResponse