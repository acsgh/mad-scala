package com.acs.scala.server.mad.provider.jetty

import javax.servlet.Servlet
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.session.SessionHandler

class JettyHandler(var servlet: Servlet) extends SessionHandler {
  override def doHandle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    servlet.service(request, response)
    baseRequest.setHandled(true)
  }
}