package com.acsgh.scala.mad.router.ws.convertions

import com.acsgh.scala.mad.router.ws.model.{WSResponse, WSResponseBuilder}

trait DefaultFormats {

  implicit def builderToResponse(builder: WSResponseBuilder): WSResponse = builder.build

}
