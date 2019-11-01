package acsgh.mad.scala.router.http

package object model {

  type RouteAction = RequestContext => Response

  type FilterAction = RequestContext => RouteAction => Response

}
