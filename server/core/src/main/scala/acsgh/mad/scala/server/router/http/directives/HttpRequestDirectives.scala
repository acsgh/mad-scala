package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.params.HttpDefaultParamHandling

trait HttpRequestDirectives extends HttpDefaultParamHandling
  with HttpDirectivesBase
  with HttpRequestPathParamsDirectives
  with HttpRequestHeaderDirectives
  with HttpRequestCookieDirectives
  with HttpRequestFormDirectives
  with HttpRouteDirectives
  with HttpRequestBody