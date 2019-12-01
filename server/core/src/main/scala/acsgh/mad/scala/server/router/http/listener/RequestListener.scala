package acsgh.mad.scala.server.router.http.listener

import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

trait RequestListener extends HttpDirectives {
  def onStart()(implicit requestContext: HttpRequestContext): Unit

  def onStop()(implicit requestContext: HttpRequestContext): Unit

  def onTimeout()(implicit requestContext: HttpRequestContext): Unit

  def onException(exception: Exception)(implicit requestContext: HttpRequestContext): Unit
}