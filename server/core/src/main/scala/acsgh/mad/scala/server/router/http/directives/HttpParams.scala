package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.server.router.http.convertions.HttpParamReader

trait Param[I, O, R] {

  val name: String

  def apply(paramType: String, input: List[I]): R
}

case class SingleParam[I, O](name: String)(implicit reader: HttpParamReader[I, O]) extends Param[I, O, O] {
  override def apply(paramType: String, input: List[I]): O = {
    try {
      input.headOption.map(reader.read) match {
        case Some(v) => v
        case _ => throw new BadRequestException(s"""Missing mandatory ${paramType.toLowerCase} param: "$name"""");
      }

    } catch {
      case e: BadRequestException => throw e;
      case e: Throwable => throw new BadRequestException(s"""Invalid ${paramType.toLowerCase} param: "$name"""", e);
    }
  }
}

case class DefaultParam[I, O](name: String, defaultValue: O)(implicit reader: HttpParamReader[I, O]) extends Param[I, O, O] {
  override def apply(paramType: String, input: List[I]): O = {
    try {
      input.headOption.map(reader.read) match {
        case Some(v) => v
        case _ => defaultValue
      }

    } catch {
      case e: BadRequestException => throw e;
      case e: Throwable => throw new BadRequestException(s"""Invalid ${paramType.toLowerCase} param: "$name"""", e);
    }
  }
}

case class OptionParam[I, O](name: String)(implicit reader: HttpParamReader[I, O]) extends Param[I, O, Option[O]] {
  override def apply(paramType: String, input: List[I]): Option[O] = {
    try {
      input.headOption.map(reader.read)
    } catch {
      case e: BadRequestException => throw e;
      case e: Throwable => throw new BadRequestException(s"""Invalid ${paramType.toLowerCase} param: "$name"""", e);
    }
  }
}

case class ListParam[I, O](name: String)(implicit reader: HttpParamReader[I, O]) extends Param[I, O, List[O]] {
  override def apply(paramType: String, input: List[I]): List[O] = {
    try {
      input.map(reader.read)
    } catch {
      case e: BadRequestException => throw e;
      case e: Throwable => throw new BadRequestException(s"""Invalid ${paramType.toLowerCase} param: "$name"""", e);
    }
  }
}