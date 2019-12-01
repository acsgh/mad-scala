package acsgh.mad.scala.server.converter.json.jackson

case class JsonErrorCode
(
  code: Int,
  status: String,
  message: Option[String]
)
