package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.exception.BadRequestException


case class Params(private val params: Map[String, List[String]] = Map()) {
  def keys: Set[String] = params.keys.toSet

  def list[T](key: String)(implicit converter: String => T): List[T] = params.getOrElse(key, List()).map(converter)

  def opt[T](key: String)(implicit converter: String => T): Option[T] = params.get(key).flatMap(_.headOption.map(converter))

  def get[T](key: String)(implicit converter: String => T): T = opt(key)(converter) match {
    case Some(v) => v
    case _ => throw BadRequestException(s"Parameter $key not found");
  }

  def get[T](key: String, defaultValue: T)(implicit converter: String => T): T = opt(key)(converter).getOrElse(defaultValue)

}
