package acsgh.mad.scala.router.ws.convertions

import acsgh.mad.scala.router.ws.model.{WSResponse, WSResponseBuilder}

import scala.language.implicitConversions

trait DefaultFormats {

  implicit def builderToResponse(builder: WSResponseBuilder): WSResponse = builder.build

}
