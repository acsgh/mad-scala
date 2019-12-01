package acsgh.mad.scala.server.converter.template.thymeleaf

import enumeratum.{Enum, EnumEntry}

sealed trait ThymeleafSource extends EnumEntry

object ThymeleafSource extends Enum[ThymeleafSource] {
  val values = findValues

  case object File extends ThymeleafSource

  case object Classpath extends ThymeleafSource

}
