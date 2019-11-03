package acsgh.mad.scala.router.http.listener

import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.model.RequestContext

trait RequestListener extends Directives {
  def onStart()(implicit requestContext: RequestContext): Unit

  def onStop()(implicit requestContext: RequestContext): Unit

  def onTimeout()(implicit requestContext: RequestContext): Unit

  def onException(exception: Exception)(implicit requestContext: RequestContext): Unit
}