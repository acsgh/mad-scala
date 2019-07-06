package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.RequestMethod
import com.acs.scala.server.mad.router.directives.DefaultParamHandling

trait Routes extends DefaultFormats with DefaultParamHandling {

  protected val httpRouter: HttpRouter

  def get(uri: String)(action: RequestContext => Response): Unit = {
    httpRouter.servlet(new Route[RequestHandler](uri, Set(RequestMethod.GET), (context: RequestContext) => action(context)))
  }
//
}
