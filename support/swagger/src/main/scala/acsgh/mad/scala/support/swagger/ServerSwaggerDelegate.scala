package acsgh.mad.scala.support.swagger

import acsgh.mad.scala.ServerDelegate
import acsgh.mad.scala.router.http.model.RouteAction
import acsgh.mad.scala.support.swagger.dsl.OpenApiBuilder
import io.swagger.v3.oas.models.{OpenAPI, Operation}

trait ServerSwaggerDelegate extends ServerDelegate[ServerSwagger] with OpenApiBuilder {

  def swaggerRoutes(docPath: String = "/api-docs")(implicit openAPi: OpenAPI): Unit = server.swaggerRoutes(docPath)(openAPi)

  def options(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.options(uri, operation)(action)(openAPi)

  def get(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.get(uri, operation)(action)(openAPi)

  def head(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.head(uri, operation)(action)(openAPi)

  def post(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.post(uri, operation)(action)(openAPi)

  def put(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.put(uri, operation)(action)(openAPi)

  def patch(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.patch(uri, operation)(action)(openAPi)

  def delete(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.delete(uri, operation)(action)(openAPi)

  def trace(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = server.trace(uri, operation)(action)(openAPi)
}
