package com.acs.scala.server.mad.provider.servlet

import com.acs.scala.server.mad.LogSupport
import com.acs.scala.server.mad.router.model.{Response, ResponseStatus}
import com.acs.scala.server.mad.router.HttpRouter
import javax.servlet._
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class MadServerFilter(private val httpRouter: HttpRouter) extends Filter with LogSupport {
  override def init(filterConfig: FilterConfig): Unit = {
  }

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain): Unit = {
    val response: Response = httpRouter.process(ServletUtils.toWaveRequest(servletRequest.asInstanceOf[HttpServletRequest]))

    if (response.responseStatus != ResponseStatus.NOT_FOUND) {
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
    val response = httpRouter.process(ServletUtils.toWaveRequest(servletRequest))
    ServletUtils.transferParams(response, servletResponse)
  }
}
