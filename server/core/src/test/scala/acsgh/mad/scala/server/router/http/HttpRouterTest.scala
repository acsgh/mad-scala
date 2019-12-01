package acsgh.mad.scala.server.router.http

import java.net.URI

import acsgh.mad.scala.core.http.exception.BadRequestException
import acsgh.mad.scala.core.http.model.{HttpRequest, ProtocolVersion, RequestMethod, ResponseStatus}
import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import org.scalatest._

import scala.language.reflectiveCalls

class HttpRouterTest extends FlatSpec with Matchers with HttpDefaultFormats {

  "HttpRouter" should "return 404 if no route" in {
    val router = new HttpRouterBuilder()
    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_FOUND)
  }

  it should "return 500 if error" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      throw new RuntimeException("aaaaaa")
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.INTERNAL_SERVER_ERROR)
  }

  it should "return 400 if bad request" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      throw new BadRequestException("aaaaaa")
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "handle a fix request" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/hello"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "Hello there"
    router.get("/hello") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a param request" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/persons/1/name"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "1"
    router.get("/persons/{id}/name") { implicit ctx =>
      val id = ctx.pathParams("id")
      ctx.response.body(id.getBytes("UTF-8"))
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a filter" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/persons/1"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )


    router.filter("/*") { implicit ctx =>
      nextJump =>
        ctx.response.status(ResponseStatus.CREATED)
        nextJump(ctx)
    }

    val body = "Hi there"
    router.get("/persons/*") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.CREATED)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }

  it should "handle a wildcard request" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/persons/1"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    val body = "Hi there"
    router.get("/persons/*") { implicit ctx =>
      ctx.response.body(body.getBytes("UTF-8"))
    }

    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be(body)
  }
}
