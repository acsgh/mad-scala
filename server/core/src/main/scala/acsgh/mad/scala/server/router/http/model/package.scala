package acsgh.mad.scala.server.router.http

import acsgh.mad.scala.core.http.model.HttpResponse


package object model {

  type RouteAction = HttpRequestContext => HttpResponse

  type FilterAction = HttpRequestContext => RouteAction => HttpResponse

}
