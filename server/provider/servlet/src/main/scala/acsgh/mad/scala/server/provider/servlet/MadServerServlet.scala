package acsgh.mad.scala.server.provider.servlet

import acsgh.mad.scala.server.router.http.HttpRouter
import com.acsgh.common.scala.log.LogSupport
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class MadServerServlet(private val httpRouter: HttpRouter) extends HttpServlet with LogSupport with MadConverter {

  override protected def service(servletRequest: HttpServletRequest, servletResponse: HttpServletResponse): Unit = {
    val response = httpRouter.process(toMadRequest(servletRequest))
    transferParams(response, servletResponse)
  }
}
