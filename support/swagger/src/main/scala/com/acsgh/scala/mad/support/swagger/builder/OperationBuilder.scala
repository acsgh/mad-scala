package com.acsgh.scala.mad.support.swagger.builder

import io.swagger.v3.oas.models.Operation

case class OperationBuilder(parent: OpenApiBuilder, protected val delegate: Operation = new Operation()) extends Builder[OpenApiBuilder, Operation] {


}
