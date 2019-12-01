package acsgh.mad.scala.server.router.http

import acsgh.mad.scala.core.http.model.HttpResponse


package object model {

  type HttpRouteAction = HttpRequestContext => HttpResponse

  type HttpFilterAction = HttpRequestContext => HttpRouteAction => HttpResponse

}
