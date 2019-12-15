package acsgh.mad.scala.examples.jetty

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

import acsgh.mad.scala.core.http.model.ResponseStatus._
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.spray.SprayDirectives
import acsgh.mad.scala.server.converter.template.thymeleaf.{ThymeleafDirectives, ThymeleafEngineProvider, ThymeleafTemplate}
import acsgh.mad.scala.server.support.swagger.ControllerSwagger
import com.acsgh.common.scala.time.TimerSplitter
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.media.NumberSchema
import org.thymeleaf.TemplateEngine

case class PersonRoutes(builder: ServerBuilder) extends ControllerSwagger with JsonProtocol with ThymeleafDirectives with SprayDirectives {

  implicit protected val thymeleafEngine: TemplateEngine = ThymeleafEngineProvider.build("/templates/")

  implicit protected val openAPi: OpenAPI = OpenAPI(
    info = Info(
      title = "Jetty Example",
      description = "Jetty example rest api example",
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


  resourceFolder("/", "public")
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
    requestBody[String] { asd=>


    requestParam("id".as[Long]) { id =>
      requestJson(classOf[Person]) { personNew =>
        persons.get(id).fold(error(NO_CONTENT)) { personOld =>
          val result = personNew.copy(id = id)
          persons = persons + (result.id -> result)
          responseJson(result)
        }
      }
    }
  }}

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

  get("/slow", Operation(
    operationId = "getSlowOperation",
    summary = "Get person",
    parameters = List(
      QueryParameter("time", "The time in milliseconds", schema = new NumberSchema)
    ),
    responses = ApiResponses(
      OK -> ApiResponseJson(classOf[Person], "The response"),
    )
  )) { implicit context =>
    requestQuery("time".as[Long].default(100)) { time =>
      Thread.sleep(time)
      responseBody(s"Response took: ${TimerSplitter.getIntervalInfo(System.currentTimeMillis() - context.request.starTime, TimeUnit.MILLISECONDS)}")
    }
  }

  ws("/echo") { implicit context =>
    wsRequest[String] { input =>
      wsResponse(s"You said: $input")
    }
  }

  filter("/*") { implicit ctx =>
    nextJump =>
      log.info("Handling: {}", ctx.request.uri)
      val result = nextJump(ctx)
      log.info("Handling: {} - {}, done", ctx.request.uri, ctx.response.responseStatus)
      result
  }
}
