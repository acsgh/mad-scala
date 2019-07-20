package com.acsgh.scala.mad.examples.jetty

import java.util.concurrent.atomic.AtomicLong

import com.acsgh.scala.mad.converter.json.jackson.JacksonHttpServer
import com.acsgh.scala.mad.converter.template.thymeleaf.ThymeleafHttpServer
import com.acsgh.scala.mad.provider.jetty.JettyServer
import com.acsgh.scala.mad.router.http.model.ResponseStatus
import com.acsgh.scala.mad.support.swagger.SwaggerRoutes
import com.acsgh.scala.mad.support.swagger.dsl._

object JettyBoot extends JettyServer with ThymeleafHttpServer with JacksonHttpServer with SwaggerRoutes {
  override val name: String = "Jetty Boot Example"

  override protected val httpPort: Option[Int] = Some(7654)
  override protected val prefix: String = "/templates/"

  implicit protected val openApi: OpenAPI = OpenAPI(
    info = Info(
      title = "Jetty Example",
      description = "Jetty example rest api example",
      version = "1.0",
      contact = Contact(
        email = "dummy@asd.com"
      )
    ),
    servers = List(
      Server(
        url = "http://www.example.com",
        variables = ServerVariables(
          Map(
            "version" -> ServerVariable(
              enum = Set("1.0", "2.0"),
              default = "1.0"
            )
          )
        )
      )
    )
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
      thymeleafTemplate("index", Map("name" -> name))
    }
  }

  get("/persons") { implicit context =>
    responseJson(persons.values)
  }

  post("/persons") { implicit context =>
    requestJson(classOf[Person]) { person =>
      val personWithId = person.copy(id = ids.addAndGet(1))
      persons = persons + (personWithId.id -> personWithId)

      responseStatus(ResponseStatus.CREATED) {
        responseJson(personWithId)
      }
    }
  }

  put("/persons/{id}") { implicit context =>
    requestParam("id".as[Long]) { id =>
      requestJson(classOf[Person]) { personNew =>
        persons.get(id).fold(error(ResponseStatus.NOT_FOUND)) { personOld =>
          val result = personNew.copy(id = id)
          persons = persons + (result.id -> result)
          responseJson(result)
        }
      }
    }
  }

  get("/persons/{id}") { implicit context =>
    requestParam("id".as[Long]) { id =>
      persons.get(id).fold(error(ResponseStatus.NOT_FOUND)) { personOld =>
        responseJson(personOld)
      }
    }
  }

  delete("/persons/{id}") { implicit context =>
    requestParam("id".as[Long]) { id =>
      persons.get(id).fold(error(ResponseStatus.NOT_FOUND)) { personOld =>
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


  filter("/*") { implicit context =>
    nextJump =>
      log.info("Handling: {}", context.request.uri)
      val result = nextJump()
      log.info("Handling: {}, done", context.request.uri)
      result
  }
}
