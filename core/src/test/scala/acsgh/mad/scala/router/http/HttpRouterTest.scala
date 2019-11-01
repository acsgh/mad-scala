package acsgh.mad.scala.router.http

import java.net.URI

import acsgh.mad.scala.router.http.convertions.DefaultFormats
import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import org.scalatest._

import scala.language.reflectiveCalls

class HttpRouterTest extends FlatSpec with Matchers with DefaultFormats {

  def f =
    new {
      val router = new HttpRouter("test", false)
      val routes = new Routes {
        override protected val httpRouter: HttpRouter = router
      }
    }

  "HttpRouter" should "return 404 if no route" in {
    val fixture = f
    val router = fixture.router
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_FOUND)
  }

  it should "return 500 if error" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      throw new RuntimeException("aaaaaa")
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.INTERNAL_SERVER_ERROR)
  }

  it should "return 400 if bad request" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      throw new BadRequestException("aaaaaa")
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "handle a fix request" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/hello"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "Hello there"
    routes.get("/hello") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a param request" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/persons/1/name"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "1"
    routes.get("/persons/{id}/name") { implicit ctx =>
      val id = ctx.pathParams("id")
      ctx.response.body(id.getBytes("UTF-8"))
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a filter" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/persons/1"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )


    routes.filter("/*") { implicit ctx =>
      nextJump =>
        ctx.response.status(ResponseStatus.CREATED)
        nextJump(ctx)
    }

    val body = "Hi there"
    routes.get("/persons/*") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.CREATED)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a wildcard request" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/persons/1"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "Hi there"
    routes.get("/persons/*") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }
}
