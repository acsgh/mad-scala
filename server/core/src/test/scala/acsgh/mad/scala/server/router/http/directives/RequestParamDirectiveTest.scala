package acsgh.mad.scala.server.router.http.directives

import java.net.URI

import acsgh.mad.scala.core.http.model.{HttpRequest, ProtocolVersion, RequestMethod, ResponseStatus}
import acsgh.mad.scala.server.router.http.HttpRouterBuilder
import acsgh.mad.scala.server.router.http.body.writer.default._
import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.reflectiveCalls

class RequestParamDirectiveTest extends AnyFlatSpec with Matchers with HttpDefaultFormats with HttpDirectives {

  "RequestParamDirective" should "return 400 if no path" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id}") { implicit ctx =>
      requestParam("id") { path =>
        responseBody(path)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("")
  }

  it should "return 200 if path" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/1234"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id}") { implicit ctx =>
      requestParam("id") { path =>
        responseBody(path)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if path convert" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/1234"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id}") { implicit ctx =>
      requestParam("id".as[Long]) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if path list empty" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id}") { implicit ctx =>
      requestParam("id".list) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List()")
  }

  it should "return 200 if path list" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/1234"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id}") { implicit ctx =>
      requestParam("id".list) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234)")
  }

  it should "return 200 if two path" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/1234/1235"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/{id1}/{id2}") { implicit ctx =>
      requestParam("id1", "id2") { (path1, path2) =>
        responseBody(List(path1, path2).toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if default path" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestParam("id".default("1234")) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if optional path" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestParam("id".opt) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("None")
  }

  it should "return 400 if no path convert" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/?id=1234a"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestParam("id".as[Long]) { path =>
        responseBody(path.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
