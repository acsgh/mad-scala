package com.acs.scala.server.mad.router.handler

import com.acs.scala.server.mad.router.constant.ResponseStatus
import com.acs.scala.server.mad.router.{ExceptionHandler, Request, RequestContext, Response, ResponseBuilder}

object DefaultExceptionHandler {

  def stacktraceToHtml(throwable: Throwable): String = {
    var result = "<html>"
    result += "<head>"
    result += "<title>Internal Server Error</title>"
    result += "</head>"
    result += "<body>"
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
  override def handle(requestContext: RequestContext, throwable: Throwable): Response = {
    requestContext.response.status(ResponseStatus.INTERNAL_SERVER_ERROR)
    requestContext.response.body(DefaultExceptionHandler.stacktraceToHtml(throwable))
    requestContext.response.build
  }
}
