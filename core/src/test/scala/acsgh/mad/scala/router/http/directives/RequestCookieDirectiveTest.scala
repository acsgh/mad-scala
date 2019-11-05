package acsgh.mad.scala.router.http.directives

import java.net.URI

import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.http.convertions.DefaultFormats
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import org.scalatest._

import scala.language.reflectiveCalls

class RequestCookieDirectiveTest extends FlatSpec with Matchers with DefaultFormats with Directives {

  def f =
    new {
      val router = HttpRouter("test", false, 1, 0)
    }

  "RequestCookieDirective" should "return 400 if no cookie" in {
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

    router.get("/") { implicit ctx =>
      requestCookie("SessionId") { cookie =>
        responseBody(cookie)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "return 200 if cookie" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Cookie" -> List("SessionId=1234")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId") { cookie =>
        responseBody(cookie)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if cookie convert" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Cookie" -> List("SessionId=1234")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".as[Long]) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if cookie list empty" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      //      Map("Cookie" -> List("SessionId=1234")),
      Map(),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".list) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List()")
  }

  it should "return 200 if cookie list" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Cookie" -> List("SessionId=1234;SessionId=1235")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".list) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if two cookie" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Cookie" -> List("SessionId1=1234;SessionId2=1235")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId1", "SessionId2") { (cookie1, cookie2) =>
        responseBody(List(cookie1, cookie2).toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if default cookie" in {
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

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".default("1234")) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if optional cookie" in {
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

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".opt) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("None")
  }

  it should "return 400 if no cookie convert" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("Cookie" -> List("SessionId=1234a")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestCookie("SessionId".as[Long]) { cookie =>
        responseBody(cookie.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
