package com.acs.scala.server.mad.router

import com.acs.scala.server.mad.router.constant.RequestMethod

trait Routes extends DefaultFormats {
  protected val httpRouter: HttpRouter

  def get(uri: String)(action: (Request, ResponseBuilder) => Response): Unit = {
    httpRouter.servlet(new Route[RequestHandler](uri, Set(RequestMethod.GET), (request: Request, responseBuilder: ResponseBuilder) => action(request, responseBuilder)))
  }
//
}
