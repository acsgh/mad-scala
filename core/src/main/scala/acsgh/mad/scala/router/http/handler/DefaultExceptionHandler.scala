package acsgh.mad.scala.router.http.handler

import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.model.ResponseStatus.{BAD_REQUEST, INTERNAL_SERVER_ERROR}
import acsgh.mad.scala.router.http.model.{RequestContext, Response, ResponseStatus}

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
  override def handle(throwable: Throwable)(implicit ctx: RequestContext): Response = {
    val status = if (throwable.isInstanceOf[BadRequestException]) BAD_REQUEST else INTERNAL_SERVER_ERROR
    responseStatus(status) {
      responseBody(getStatusBody(status, throwable))
    }
  }

  protected def getStatusBody(status: ResponseStatus, throwable: Throwable)(implicit ctx: RequestContext): String = {
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
