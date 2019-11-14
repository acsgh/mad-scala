package acsgh.mad.scala.converter.json.jackson

case class JsonErrorCode
(
  code: Int,
  status: String,
  message: Option[String]
)
