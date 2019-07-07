package com.acsgh.scala.mad.examples.netty

import spray.json.DefaultJsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat = jsonFormat3(Person)

}
