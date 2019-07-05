package com.acs.scala.server.mad.router.exception

class BadRequestException(message: String) extends RuntimeException(message)

class ParameterException(message: String) extends RuntimeException(message)

class ParameterNotFoundException(parameterKey: String, parameterType: String = "Parameter") extends RuntimeException(s"$parameterType '$parameterKey' not found")

class UnexpectedContentTypeException(contentType: String) extends RuntimeException(s"Unexpected content type '$contentType'")
