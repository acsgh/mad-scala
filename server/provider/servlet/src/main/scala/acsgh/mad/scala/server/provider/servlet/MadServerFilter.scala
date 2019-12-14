package acsgh.mad.scala.server.provider.servlet

import acsgh.mad.scala.core.http.model.HttpResponse
import acsgh.mad.scala.core.http.model.ResponseStatus._
import acsgh.mad.scala.server.router.http.HttpRouter
import com.acsgh.common.scala.log.LogSupport
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class MadServerFilter(private val httpRouter: HttpRouter) extends Filter with LogSupport with MadConverter {
  override def init(filterConfig: FilterConfig): Unit = {
  }

  override def doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, chain: FilterChain): Unit = {
    val response: HttpResponse = httpRouter.process(toMadRequest(servletRequest.asInstanceOf[HttpServletRequest]))

    if (response.responseStatus != NOT_FOUND) {
      transferParams(response, servletResponse.asInstanceOf[HttpServletResponse])
    } else {
      chain.doFilter(servletRequest, servletResponse)
    }
  }

  override def destroy(): Unit = {
  }
}
