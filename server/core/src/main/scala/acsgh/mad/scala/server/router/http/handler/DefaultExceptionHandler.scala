package acsgh.mad.scala.server.router.http.handler

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.ResponseStatus._
import acsgh.mad.scala.core.http.model.{HttpResponse, ResponseStatus}
import acsgh.mad.scala.server.router.http.body.writer.default._
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

object DefaultExceptionHandler {

  def stacktraceToHtml(throwable: Throwable): String = {
    var result = "<p>"
    result += stacktraceToHtmlInternal(throwable, causeThrowable = false)
    var cause = throwable.getCause

    while (cause != null) {
      result += stacktraceToHtmlInternal(cause, causeThrowable = true)
      cause = cause.getCause
    }

    result += "</p>"
    result
  }

  def stacktraceToPlain(throwable: Throwable): String = {
    var result = ""
    result += stacktraceToPlainInternal(throwable, causeThrowable = false)
    var cause = throwable.getCause

    while (cause != null) {
      result += stacktraceToPlainInternal(cause, causeThrowable = true)
      cause = cause.getCause
    }

    result
  }

  private def stacktraceToHtmlInternal(throwable: Throwable, causeThrowable: Boolean) = {
    var result = ""
    result += "<b>"
    if (causeThrowable) {
      result += "Caused by: "
    }
    result += throwable.getClass.getName + ":&nbsp;" + "</b>" + throwable.getMessage + "<br/>\n"
    for (stackTraceElement <- throwable.getStackTrace) {
      result += "&nbsp;&nbsp;&nbsp;&nbsp;" + stackTraceElement + "<br/>\n"
    }
    result
  }

  private def stacktraceToPlainInternal(throwable: Throwable, causeThrowable: Boolean) = {
    var result = if (causeThrowable) "Caused by: " else ""

    result += throwable.getClass.getName + " " + throwable.getMessage + "\n"
    for (stackTraceElement <- throwable.getStackTrace) {
      result += "\t" + stackTraceElement + "\n"
    }
    result
  }
}

class DefaultExceptionHandler() extends ExceptionHandler {
  override def handle(throwable: Throwable)(implicit ctx: HttpRequestContext): HttpResponse = {
    val status = if (throwable.isInstanceOf[BadRequestException]) BAD_REQUEST else INTERNAL_SERVER_ERROR
    responseStatus(status) {
      responseBody(getStatusBody(status, throwable))
    }
  }

  protected def getStatusBody(status: ResponseStatus, throwable: Throwable)(implicit ctx: HttpRequestContext): String = {
    s"""<html>
       |<head>
       |   <title>${status.code} - ${status.message}</title>
       |</head>
       |<body>
       |   <h2>${status.code} - ${status.message}</h2>
       |   ${if (ctx.router.productionMode) "" else DefaultExceptionHandler.stacktraceToHtml(throwable)}
       |</body>
       |</html>""".stripMargin
  }
}
