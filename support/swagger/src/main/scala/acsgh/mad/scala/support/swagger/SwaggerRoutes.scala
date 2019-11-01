package acsgh.mad.scala.support.swagger

import acsgh.mad.scala.router.http.Routes
import acsgh.mad.scala.router.http.model.RequestMethod._
import acsgh.mad.scala.router.http.model.{RequestMethod, RouteAction}
import acsgh.mad.scala.support.swagger.dsl.OpenApiBuilder
import com.acsgh.common.scala.log.LogSupport
import io.swagger.v3.core.util.{Json, Yaml}
import io.swagger.v3.oas.models.{OpenAPI, Operation, PathItem, Paths}

trait SwaggerRoutes extends Routes with OpenApiBuilder with LogSupport{

  def swaggerRoutes(docPath: String = "/api-docs")(implicit openAPi: OpenAPI): Unit = {
    webjars()
    resourceFolder("/{path+}", "swagger-ui")

    get(s"$docPath.json") { implicit context =>
      responseHeader("Content-Type", "application/json") {
        responseBody(Json.pretty().writeValueAsString(openAPi))
      }
    }

    get(s"$docPath.yaml") { implicit context =>
      responseHeader("Content-Type", "application/yaml") {
        responseBody(Yaml.pretty().writeValueAsString(openAPi))
      }
    }
  }

  def options(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, OPTIONS, operation)(action)

  def get(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, GET, operation)(action)

  def head(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, HEAD, operation)(action)

  def post(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, POST, operation)(action)

  def put(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, PUT, operation)(action)

  def patch(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, PATCH, operation)(action)

  def delete(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, DELETE, operation)(action)

  def trace(uri: String, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = servlet(uri, TRACE, operation)(action)

  protected def servlet(uri: String, method: RequestMethod, operation: Operation)(action: RouteAction)(implicit openAPi: OpenAPI): Unit = {
    if (openAPi.getPaths == null) {
      openAPi.setPaths(new Paths)
    }

    openAPi.getPaths.putIfAbsent(uri, new PathItem())

    val item = openAPi.getPaths.get(uri)

    method match {
      case RequestMethod.OPTIONS =>
        item.options(operation)
      case RequestMethod.GET =>
        item.get(operation)
      case RequestMethod.HEAD =>
        item.head(operation)
      case RequestMethod.POST =>
        item.post(operation)
      case RequestMethod.PUT =>
        item.put(operation)
      case RequestMethod.PATCH =>
        item.patch(operation)
      case RequestMethod.DELETE =>
        item.delete(operation)
      case RequestMethod.TRACE =>
        item.trace(operation)
      case method =>
        throw new Exception(s"Unknown Method: $method")
    }

    servlet(uri, method)(action)
  }
}
