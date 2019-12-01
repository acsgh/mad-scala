package acsgh.mad.scala.server.converter.json.spray

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonErrorCodeFormat extends DefaultJsonProtocol {
  implicit val jsonErrorCodeFormat: RootJsonFormat[JsonErrorCode] = jsonFormat3(JsonErrorCode)
}

case class JsonErrorCode
(
  code: Int,
  status: String,
  message: Option[String]
)
