package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import acsgh.mad.scala.server.router.http.params.HttpDefaultParamHandling

trait HttpRequestDirectives extends HttpDefaultParamHandling
  with HttpDefaultFormats
  with HttpRequestPathParamsDirectives
  with HttpRequestHeaderDirectives
  with HttpRequestCookieDirectives
  with HttpRequestFormDirectives
  with HttpRouteDirectives
  with HttpRequestBody