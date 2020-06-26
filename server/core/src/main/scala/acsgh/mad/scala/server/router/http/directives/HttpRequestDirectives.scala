package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.server.router.http.convertions.{HttpDefaultFormats, HttpDefaultParamHandling}

trait HttpRequestDirectives extends HttpDefaultParamHandling
  with HttpDefaultFormats
  with HttpRequestParamsDirectives
  with HttpRequestHeaderDirectives
  with HttpRequestQueryDirectives
  with HttpRequestCookieDirectives
  with HttpRequestFormDirectives
  with HttpRequestMultipartParamDirectives
  with HttpRouteDirectives
  with HttpRequestBody