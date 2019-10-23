package acsgh.mad.scala.router.ws.handler

import acsgh.mad.scala.router.ws.WSRequestContext
import acsgh.mad.scala.router.ws.directives.Directives
import acsgh.mad.scala.router.ws.model.WSResponse
import com.acsgh.common.scala.log.LogSupport

trait WSHandler extends LogSupport with Directives {
  def handle(implicit requestContext: WSRequestContext): Option[WSResponse]
}
