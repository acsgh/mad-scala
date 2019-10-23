package acsgh.mad.scala.router.http

import java.net.URI

import acsgh.mad.scala.router.http.exception.BadRequestException
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import org.scalatest._

class HttpRouterTest extends FlatSpec with Matchers {

  def f =
    new {
      val router = new HttpRouter with Routes {
        override protected val httpRouter: HttpRouter = this
      }
    }

  "HttpRouter" should "return 404 if no route" in {
    val router = f.router
    val request = new Request(
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
    val router = f.router
    val request = new Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/"){implicit ctx =>
      throw new RuntimeException("aaaaaa")
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.INTERNAL_SERVER_ERROR)
  }

  it should "return 400 if bad request" in {
    val router = f.router
    val request = new Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )

    router.get("/"){implicit ctx =>
      throw new BadRequestException("aaaaaa")
    }

    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.BAD_REQUEST)
  }
}
