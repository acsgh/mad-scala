package acsgh.mad.scala.server.router.http.directives

import java.net.URI

import acsgh.mad.scala.core.http.model.{HttpRequest, ProtocolVersion, RequestMethod, ResponseStatus}
import acsgh.mad.scala.server.router.http.HttpRouterBuilder
import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.reflectiveCalls

class HttpRequestBodyParamDirectivesTest extends AnyFlatSpec with Matchers with HttpDefaultFormats with HttpDirectives {

  "HttpRequestBodyParamDirectives" should "return 400 if no body param" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId") { query =>
        responseBody(query)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "return 200 if body param" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "SessionId=1234".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId") { query =>
        responseBody(query)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if query convert" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "SessionId=1234".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".as[Long]) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if query list empty" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".list) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List()")
  }

  it should "return 200 if query list" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "SessionId=1234&SessionId=1235".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".list) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if two body param" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "SessionId1=1234&SessionId2=1235".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId1", "SessionId2") { (query1, query2) =>
        responseBody(List(query1, query2).toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if default body param" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".default("1234")) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if optional body param" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".opt) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("None")
  }

  it should "return 400 if no query convert" in {
    val router = new HttpRouterBuilder()

    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Content-Type" -> List("application/x-www-form-urlencoded")),
      "SessionId=1234a".getBytes("UTF-8")
    )

    router.get("/") { implicit ctx =>
      requestBodyParam("SessionId".as[Long]) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.build("test", productionMode = false).process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
