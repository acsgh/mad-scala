package acsgh.mad.scala.router.http.directives

import java.net.URI

import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.http.convertions.DefaultFormats
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import org.scalatest._

import scala.language.reflectiveCalls

class RequestHeaderDirectiveTest extends FlatSpec with Matchers with DefaultFormats with Directives {

  def f =
    new {
      val router = HttpRouter("test", false, 1, 0)
    }

  "RequestHeaderDirective" should "return 400 if no header" in {
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
      requestHeader("SessionId") { header =>
        responseBody(header)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "return 200 if header" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("SessionId" -> List("1234")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestHeader("SessionId") { header =>
        responseBody(header)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if header convert" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("SessionId" -> List("1234")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestHeader("SessionId".as[Long]) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if header list empty" in {
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
      requestHeader("SessionId".list) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List()")
  }

  it should "return 200 if header list" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("SessionId" -> List("1234", "1235")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestHeader("SessionId".list) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if two header" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("SessionId1" -> List("1234"), "SessionId2" -> List("1235")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestHeader("SessionId1", "SessionId2") { (header1, header2) =>
        responseBody(List(header1, header2).toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if default header" in {
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
      requestHeader("SessionId".default("1234")) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if optional header" in {
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
      requestHeader("SessionId".opt) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("None")
  }

  it should "return 400 if no header convert" in {
    val fixture = f
    val router = fixture.router

    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map("SessionId" -> List("1234a")),
      new Array[Byte](0)
    )

    router.get("/") { implicit ctx =>
      requestHeader("SessionId".as[Long]) { header =>
        responseBody(header.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
