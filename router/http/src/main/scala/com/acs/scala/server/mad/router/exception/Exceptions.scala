package com.acs.scala.server.mad.router.exception

case class BadRequestException(message: String) extends RuntimeException(message)

case class UnexpectedContentTypeException(contentType: String) extends RuntimeException(s"Unexpected content type '$contentType'")
