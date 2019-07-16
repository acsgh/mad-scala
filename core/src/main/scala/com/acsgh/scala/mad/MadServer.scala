package com.acsgh.scala.mad

import com.acsgh.common.scala.App
import com.acsgh.common.scala.log.LogSupport

trait MadServer extends App with ProductionInfo with LogSupport {

  val host: String = "0.0.0.0"
}
