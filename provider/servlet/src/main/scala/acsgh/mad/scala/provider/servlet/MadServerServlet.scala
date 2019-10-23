package acsgh.mad.scala.provider.servlet

import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.http.model.Response
import acsgh.mad.scala.router.http.model.ResponseStatus._
import com.acsgh.common.scala.log.LogSupport
import javax.servlet._
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class MadServerFilter(private val httpRouter: HttpRouter) extends Filter with LogSupport {
  override def init(filterConfig: FilterConfig): Unit = {
  }

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain): Unit = {
    val response: Response = httpRouter.process(ServletUtils.toMadRequest(servletRequest.asInstanceOf[HttpServletRequest]))

    if (response.responseStatus != NOT_FOUND) {
      ServletUtils.transferParams(response, servletResponse.asInstanceOf[HttpServletResponse])
    } else {
      chain.doFilter(servletRequest, servletResponse)
    }
  }

  override def destroy(): Unit = {
  }
}

class MadServerServlet(private val httpRouter: HttpRouter) extends HttpServlet with LogSupport {

  override protected def service(servletRequest: HttpServletRequest, servletResponse: HttpServletResponse): Unit = {
    val response = httpRouter.process(ServletUtils.toMadRequest(servletRequest))
    ServletUtils.transferParams(response, servletResponse)
  }
}
