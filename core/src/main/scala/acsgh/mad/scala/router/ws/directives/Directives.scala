package acsgh.mad.scala.router.ws.directives

import acsgh.mad.scala.router.ws.convertions.{BodyReader, BodyWriter, DefaultFormats}
import acsgh.mad.scala.router.ws.model._

trait Directives extends DefaultFormats {
  def wsRequest[T](action: T => Option[WSResponse])(implicit context: WSRequestContext, reader: BodyReader[T]): Option[WSResponse] = context.request match {
    case t: WSRequestText => action(reader.read(t.text))
    case t: WSRequestBinary => action(reader.read(t.bytes))
    case _ => None // Do nothing
  }

  def wsResponse[T](input: T)(implicit context: WSRequestContext, writer: BodyWriter[T]): Option[WSResponse] = context.request match {
    case _: WSRequestText => wsResponseText(input)
    case _: WSRequestBinary => wsResponseBytes(input)
    case _ => None // Do nothing
  }

  def wsResponseText[T](input: T)(implicit context: WSRequestContext, writer: BodyWriter[T]): Option[WSResponse] = Some(WSResponseText(writer.writeText(input)))

  def wsResponseBytes[T](input: T)(implicit context: WSRequestContext, writer: BodyWriter[T]): Option[WSResponse] = Some(WSResponseBinary(writer.writeBytes(input)))

  def close()(implicit context: WSRequestContext): Option[WSResponse] = Some(WSResponseClose())
}
