package acsgh.mad.scala.examples.netty

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat: RootJsonFormat[Person] = jsonFormat3(Person)

}
