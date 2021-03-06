package acsgh.mad.scala.core.http.model

import enumeratum._

sealed trait Protocol extends EnumEntry

object Protocol extends Enum[Protocol] {
  val values = findValues

  case object HTTP extends Protocol

  case object HTTPS extends Protocol

  case object WS extends Protocol

  case object WSS extends Protocol

}