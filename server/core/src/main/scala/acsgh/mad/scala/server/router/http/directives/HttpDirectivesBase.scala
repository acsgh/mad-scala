package acsgh.mad.scala.server.router.http.directives

import acsgh.mad.scala.core.http.model.{HttpResponse, HttpResponseBuilder}

import scala.language.implicitConversions

trait HttpDirectivesBase {

  implicit def builderToResponse(response: HttpResponseBuilder): HttpResponse = response.build
}
