package acsgh.mad.scala.server.router.http.convertions

import acsgh.mad.scala.core.http.model.{HttpResponse, HttpResponseBuilder}
import acsgh.mad.scala.server.router.http.model.HttpRequestContext

import scala.language.implicitConversions

trait HttpDefaultFormats {

  implicit def builderToResponse(response: HttpResponseBuilder): HttpResponse = response.build
}
