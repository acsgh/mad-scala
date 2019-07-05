package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.exception.ParameterNotFoundException

case class HTTPParams(private val params: Map[String, String] = Map()) {
  def keys: Set[String] = params.keys.toSet

  def opt[T](key: String)(implicit converter: String => T): Option[T] = params.get(key).map(converter)

  def get[T](key: String)(implicit converter: String => T): T = opt(key)(converter) match {
    case Some(v) => v
    case _ => throw new ParameterNotFoundException(key);
  }

  def get[T](key: String, defaultValue: T)(implicit converter: String => T): T = opt(key)(converter).getOrElse(defaultValue)

}
