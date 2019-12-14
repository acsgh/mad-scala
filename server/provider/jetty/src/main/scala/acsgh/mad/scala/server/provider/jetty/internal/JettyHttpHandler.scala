package acsgh.mad.scala.server.provider.jetty.internal

import acsgh.mad.scala.server.router.ws.WSRouter
import javax.servlet.Servlet
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.session.SessionHandler

class JettyHttpHandler(var servlet: Servlet, var wsRouter: WSRouter) extends SessionHandler {
  override def doHandle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse): Unit = {
    if (!wsRouter.wsRoutes.contains(request.getRequestURI)) {
      servlet.service(request, response)
      baseRequest.setHandled(true)
    }
  }
}
