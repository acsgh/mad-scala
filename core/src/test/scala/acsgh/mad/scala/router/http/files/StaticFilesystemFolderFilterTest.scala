package acsgh.mad.scala.router.http.files

import java.net.URI

import acsgh.mad.scala.router.http.convertions.DefaultFormats
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, ResponseStatus}
import acsgh.mad.scala.router.http.{HttpRouter, Routes}
import org.scalatest._

import scala.language.reflectiveCalls

class StaticFilesystemFolderFilterTest extends FlatSpec with Matchers with DefaultFormats {

  def f =
    new {
      val baseFolder = "core/src/test/resources/assets"
      val router = new HttpRouter("test", false, 1, 0)
      val routes = new Routes {
        override protected val httpRouter: HttpRouter = router
      }
    }

  "StaticFilesystemFolderFilter" should "return 404 if no file" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    routes.filesystemFolder("/assets2", f.baseFolder)
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/assets2/home.html"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )
    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_FOUND)
  }

  it should "return 200 if file" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    routes.filesystemFolder("/", f.baseFolder)
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )
    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be("Hi there!")
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.get("Last-Modified").isDefined should be(true)
  }

  it should "return 304 if file not modifier, etag" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    routes.filesystemFolder("/", f.baseFolder)
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map("If-None-Match" -> List("396199333EDBF40AD43E62A1C1397793")),
      new Array[Byte](0)
    )
    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_MODIFIED)
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.get("Last-Modified").isDefined should be(true)
  }

  it should "return 304 if file not modifier, date" in {
    val fixture = f
    val router = fixture.router
    val routes = fixture.routes
    routes.filesystemFolder("/", f.baseFolder)
    val request = Request(
      RequestMethod.GET,
      "1.2.3.4",
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map("If-Modified-Since" -> List("Mon, 28 Oct 2119 21:30:51 CET")),
      new Array[Byte](0)
    )
    val response = router.process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_MODIFIED)
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.get("Last-Modified").isDefined should be(true)
  }
}
