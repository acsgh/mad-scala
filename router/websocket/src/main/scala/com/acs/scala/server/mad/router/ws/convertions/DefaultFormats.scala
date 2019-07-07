package com.acs.scala.server.mad.router.ws.convertions

import com.acs.scala.server.mad.router.ws.model.{WSResponse, WSResponseBuilder}

trait DefaultFormats {

  implicit def builderToResponse(builder: WSResponseBuilder): WSResponse = builder.build

}
