package acsgh.mad.scala.examples.netty

import java.util.concurrent.atomic.AtomicLong

import acsgh.mad.scala.Server
import acsgh.mad.scala.converter.json.spray.SprayDirectives
import acsgh.mad.scala.converter.template.thymeleaf.{ThymeleafHttpServer, ThymeleafTemplate}
import acsgh.mad.scala.router.http.model.ResponseStatus._
import acsgh.mad.scala.support.swagger.SwaggerRoutes
import com.acsgh.common.scala.App
import io.swagger.v3.oas.models.OpenAPI

object NettyBoot extends Server with App with ThymeleafHttpServer with JsonProtocol with SprayDirectives with SwaggerRoutes {
  override val name: String = "Netty Boot Example"

  override protected val prefix: String = "/templates/"

  implicit protected val openApi: OpenAPI = OpenAPI(
    info = Info(
      title = "Netty Example",
      description = "Netty example rest api example",
      version = "1.0",
      contact = Contact(
        email = "dummy@asd.com"
      )
    ),
  )


  swaggerRoutes()

  val ids = new AtomicLong(0)

  private var persons: Map[Long, Person] = {
    (0 until 10).map { _ =>
      val id = ids.addAndGet(1)
      (id, Person(id, "John Doe " + id, 2 * id))
    }.toMap
  }


  resourceFolder("/*", "public")
  webjars()

  get("/") { implicit context =>
    requestQuery("name".default("Jonh Doe")) { name =>
      responseBody(ThymeleafTemplate("index", Map("name" -> name)))
    }
  }

  get("/persons", Operation(
    operationId = "findAll",
    summary = "Get All",
    description = "Get all persons of the service",
    responses = ApiResponses(
      OK -> ApiResponseJson(classOf[List[Person]], "All persons"),
    )
  )) { implicit context =>
    responseJson(persons.values.toList.sortBy(_.id))
  }

  post("/persons", Operation(
    operationId = "createPerson",
    summary = "Create person",
    description = "Get all persons of the service",
    requestBody = RequestBodyJson(
      classOf[Person],
      example = Person(
        123,
        "Alberto",
        32
      )
    ),
    responses = ApiResponses(
      CREATED -> ApiResponseJson(classOf[Person], "The person created"),
      BAD_REQUEST -> ApiResponse("Invalid request")
    )
  )) { implicit context =>
    requestJson(classOf[Person]) { person =>
      val personWithId = person.copy(id = ids.addAndGet(1))
      persons = persons + (personWithId.id -> personWithId)

      responseStatus(CREATED) {
        responseJson(personWithId)
      }
    }
  }

  put("/persons/{id}", Operation(
    operationId = "editPerson",
    summary = "Edit person",
    parameters = List(
      PathParameter("id", "The person id")
    ),
    requestBody = RequestBodyJson(
      classOf[Person],
      example = Person(
        123,
        "Alberto",
        32
      )
    ),
    responses = ApiResponses(
      CREATED -> ApiResponseJson(classOf[Person], "The person modified"),
      NO_CONTENT -> ApiResponse("Person not found"),
      BAD_REQUEST -> ApiResponse("Invalid request")
    )
  )) { implicit context =>
    requestParam("id".as[Long]) { id =>
      requestJson(classOf[Person]) { personNew =>
        persons.get(id).fold(error(NO_CONTENT)) { personOld =>
          val result = personNew.copy(id = id)
          persons = persons + (result.id -> result)
          responseJson(result)
        }
      }
    }
  }

  get("/persons/{id}", Operation(
    operationId = "getPerson",
    summary = "Get person",
    parameters = List(
      PathParameter("id", "The person id")
    ),
    responses = ApiResponses(
      OK -> ApiResponseJson(classOf[Person], "The person"),
      NO_CONTENT -> ApiResponse("Person not found"),
      BAD_REQUEST -> ApiResponse("Invalid request")
    )
  )) { implicit context =>
    requestParam("id".as[Long]) { id =>
      persons.get(id).fold(error(NO_CONTENT)) { personOld =>
        responseJson(personOld)
      }
    }
  }

  delete("/persons/{id}", Operation(
    operationId = "deletePerson",
    summary = "Delete person",
    parameters = List(
      PathParameter("id", "The person id")
    ),
    responses = ApiResponses(
      OK -> ApiResponseJson(classOf[Person], "The person deleted"),
      NO_CONTENT -> ApiResponse("Person not found"),
      BAD_REQUEST -> ApiResponse("Invalid request")
    )
  )) { implicit context =>
    requestParam("id".as[Long]) { id =>
      persons.get(id).fold(error(NO_CONTENT)) { personOld =>
        persons = persons - id
        responseJson(personOld)
      }
    }
  }

  ws("/echo") { implicit context =>
    requestString { input =>
      responseBody(s"You said: $input")
    }
  }

  //  implicit class FutureRoute(val a: Future[RouteResult]) extends Route[Future] {
  //    override def run(ctx: RequestContext): RouteResult = RouteMessage(ResponseStatus.OK, Some("asd"))
  //  }
  //
  //  before("/*"){implicit ctx =>
  //    ctx.response
  //  }

  //  before("/*"){implicit ctx =>
  //    Future{
  //      ctx.response
  //    }
  //  }


  filter("/*") { implicit context =>
    nextJump =>
      log.info("Handling: {}", context.request.uri)
      val result = nextJump()
      log.info("Handling: {} - {}, done", context.request.uri, context.response.responseStatus)
      result
  }
}
