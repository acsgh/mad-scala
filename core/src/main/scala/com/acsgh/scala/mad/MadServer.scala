package com.acsgh.scala.mad

trait MadServer extends App with ProductionInfo with LogSupport {

  val host: String = "0.0.0.0"
}
