package com.acsgh.scala.mad.support.swagger.dsl

package object model {
  type SecurityRequirement = Map[String, List[String]]
  type Content = Map[String, MediaType]
  type ApiResponses = Map[String, ApiResponse]
}
