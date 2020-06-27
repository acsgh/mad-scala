package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.{HttpResponse, HttpResponseBuilder}

trait HttpDirectivesBase {

  implicit def builderToResponse(response: HttpResponseBuilder): HttpResponse = response.build
}
