package com.acs.scala.server.mad.router.exception

case class BadRequestException(message: String) extends RuntimeException(message)

case class ParameterException(message: String) extends RuntimeException(message)

case class ParameterNotFoundException(parameterKey: String, parameterType: String = "Parameter") extends RuntimeException(s"$parameterType '$parameterKey' not found")

case class UnexpectedContentTypeException(contentType: String) extends RuntimeException(s"Unexpected content type '$contentType'")
