package acsgh.mad.scala.router.http.directives

import java.net.URI

import acsgh.mad.scala.router.http.convertions.DefaultFormats
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import acsgh.mad.scala.router.http.{HttpRouter, Routes}
import org.scalatest._

import scala.language.reflectiveCalls

class RequestQueryDirectiveTest extends FlatSpec with Matchers with DefaultFormats with Directives {

  def f =
    new {
      val router = new HttpRouter("test", false, 1, 0)
      val routes = new Routes {
        override protected val httpRouter: HttpRouter = router
      }
    }

  "RequestQueryDirective" should "return 400 if no query" in {
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
      requestParam("SessionId") { query =>
        responseBody(query)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }

  it should "return 200 if query" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/?SessionId=1234"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      requestQuery("SessionId") { query =>
        responseBody(query)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if query convert" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/?SessionId=1234"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      requestQuery("SessionId".as[Long]) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if query list empty" in {
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
      requestQuery("SessionId".list) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List()")
  }

  it should "return 200 if query list" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/?SessionId=1234&SessionId=1235"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      requestQuery("SessionId".list) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if two query" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/?SessionId1=1234&SessionId2=1235"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      requestQuery("SessionId1", "SessionId2") { (query1, query2) =>
        responseBody(List(query1, query2).toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("List(1234, 1235)")
  }

  it should "return 200 if default query" in {
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
      requestQuery("SessionId".default("1234")) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("1234")
  }

  it should "return 200 if optional query" in {
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
      requestQuery("SessionId".opt) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTf-8") should be("None")
  }

  it should "return 400 if no query convert" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/?SessionId=1234a"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    routes.get("/") { implicit ctx =>
      requestQuery("SessionId".as[Long]) { query =>
        responseBody(query.toString)
      }
    }

    val response = router.process(request)

    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
