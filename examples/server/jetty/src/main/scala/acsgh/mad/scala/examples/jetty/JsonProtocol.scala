package acsgh.mad.scala.examples.jetty

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val personFormat: RootJsonFormat[Person] = jsonFormat3(Person)

}
