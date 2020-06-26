package acsgh.mad.scala.server.router.http.directives

case class UrlFormEncodedBody
(
  params: Map[String, List[String]]
)

case class Multipart
(
  parts: List[MultipartPart]
)

case class MultipartPart
(
  name: String,
  contentType: Option[String],
  filename: Option[String],
  content: String
)