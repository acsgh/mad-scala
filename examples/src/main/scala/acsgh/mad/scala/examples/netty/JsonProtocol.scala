package acsgh.mad.scala.examples.netty

import spray.json.DefaultJsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat = jsonFormat3(Person)

}
