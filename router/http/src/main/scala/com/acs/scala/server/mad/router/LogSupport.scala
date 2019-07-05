package com.acs.scala.server.mad.router

import org.slf4j.{Logger, LoggerFactory}

trait LogSupport {
  val log: Logger = LoggerFactory.getLogger(getClass)
}
