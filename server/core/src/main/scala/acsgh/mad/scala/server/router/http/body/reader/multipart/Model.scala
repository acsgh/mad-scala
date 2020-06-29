package acsgh.mad.scala.server.router.http.body.reader.multipart

case class Multipart
(
  parts: List[MultipartPart]
)

case class MultipartPart
(
  name: String,
  contentType: Option[String],
  filename: Option[String],
  content: Array[Byte]
)

case class MultipartFile
(
  contentType: String,
  filename: Option[String],
  content: Array[Byte]
)
