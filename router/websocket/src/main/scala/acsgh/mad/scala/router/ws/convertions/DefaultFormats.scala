package acsgh.mad.scala.router.ws.convertions

import acsgh.mad.scala.router.ws.model.{WSResponse, WSResponseBuilder}

trait DefaultFormats {

  implicit def builderToResponse(builder: WSResponseBuilder): WSResponse = builder.build

}
