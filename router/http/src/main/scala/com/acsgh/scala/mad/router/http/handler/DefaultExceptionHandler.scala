package com.acsgh.scala.mad.router.http.handler

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.exception.BadRequestException
import com.acsgh.scala.mad.router.http.model.{Response, ResponseStatus}

object DefaultExceptionHandler {

  def stacktraceToHtml(status: ResponseStatus, throwable: Throwable): String = {
    var result = "<html>"
    result += "<head>"
    result += s"<title>${status.code} - ${status.message}</title>"
    result += "</head>"
    result += "<body>"
    result += s"<h2>${status.code} - ${status.message}</h2>"
    result += "<p>"
    result += stacktraceToHtmlInternal(throwable, causeThrowable = false)
    var cause = throwable.getCause

    while (cause != null) {
      result += stacktraceToHtmlInternal(cause, causeThrowable = true)
      cause = cause.getCause
    }

    result += "</p>"
    result += "</body>"
    result += "</html>"
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
}

class DefaultExceptionHandler extends ExceptionHandler {
  override def handle(throwable: Throwable)(implicit requestContext: RequestContext): Response = {
    val status = if (throwable.isInstanceOf[BadRequestException]) ResponseStatus.BAD_REQUEST else ResponseStatus.INTERNAL_SERVER_ERROR
    responseStatus(status) {
      responseBody(DefaultExceptionHandler.stacktraceToHtml(status, throwable))
    }
  }
}
