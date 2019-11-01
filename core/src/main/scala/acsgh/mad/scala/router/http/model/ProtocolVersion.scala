package acsgh.mad.scala.router.http.model

import enumeratum._

sealed abstract class ProtocolVersion
(
  val protocol: Protocol,
  val majorVersion: Int,
  val minorVersion: Int,
  val keepAliveDefault: Boolean,
  val bytes: Boolean,
) extends EnumEntry {
  override val entryName: String = s"$protocol/$majorVersion.$minorVersion"
}

object ProtocolVersion extends Enum[ProtocolVersion] {
  val values = findValues

  case object HTTP_1_0 extends ProtocolVersion(Protocol.HTTP, 1, 0, false, true)

  case object HTTP_1_1 extends ProtocolVersion(Protocol.HTTP, 1, 1, true, true)

}

