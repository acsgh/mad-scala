package com.acsgh.scala.mad.support.swagger.builder

import com.acsgh.scala.mad.router.http.model.RequestMethod
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.{OpenAPI, Operation}

case class OpenApiBuilder(private val delegate: OpenAPI = new OpenAPI()) {

  def info: InfoBuilder = {
    if (delegate.getInfo == null) {
      delegate.setInfo(new Info())
    }
    InfoBuilder(this, delegate.getInfo)
  }

  def addOperation(uri: String, method: RequestMethod, operation: Operation): OpenApiBuilder = {
    this
  }


  def build = delegate

}
