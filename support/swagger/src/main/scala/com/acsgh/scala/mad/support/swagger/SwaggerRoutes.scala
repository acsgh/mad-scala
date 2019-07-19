package com.acsgh.scala.mad.support.swagger

import com.acsgh.scala.mad.converter.json.jackson.JacksonDirectives
import com.acsgh.scala.mad.router.http.model.RequestMethod._
import com.acsgh.scala.mad.router.http.model.{RequestMethod, Response}
import com.acsgh.scala.mad.router.http.{HttpRouter, RequestContext, Routes}
import com.acsgh.scala.mad.support.swagger.builder.{OpenApiBuilder, OperationBuilder}
import io.swagger.v3.core.util.{Json, Yaml}
import io.swagger.v3.oas.models.{OpenAPI, Operation}

trait SwaggerRoutes extends Routes with JacksonDirectives {

  implicit protected def toOpenApi(builder: OpenApiBuilder): OpenAPI = builder.build

  implicit protected def toOperation(builder: OperationBuilder): Operation = builder.build

  def swaggerRoutes(docPath: String = "/api-docs")(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = {
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

  def options(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, OPTIONS, operation)(action)

  def get(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, GET, operation)(action)

  def head(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, HEAD, operation)(action)

  def post(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, POST, operation)(action)

  def put(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, PUT, operation)(action)

  def patch(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, PATCH, operation)(action)

  def delete(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, DELETE, operation)(action)

  def trace(uri: String, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = servlet(uri, TRACE, operation)(action)

  protected def servlet(uri: String, method: RequestMethod, operation: Operation)(action: RequestContext => Response)(implicit httpRouter: HttpRouter, openAPi: OpenApiBuilder): Unit = {
    openAPi.addOperation(uri, method, operation)
    servlet(uri, method)(action)
  }
}
