package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.model.{RequestMethod, Response}
import com.acs.scala.server.mad.router.convertions.{DefaultFormats, DefaultParamHandling}

trait Routes extends DefaultFormats with DefaultParamHandling {

  protected val httpRouter: HttpRouter

  def get(uri: String)(action: RequestContext => Response): Unit = {
    httpRouter.servlet(new HttpRoute[RequestHandler](uri, Set(RequestMethod.GET), (context: RequestContext) => action(context)))
  }
//
}
