package acsgh.mad.scala.server.router.http.files

import java.net.URI

import acsgh.mad.scala.core.http.model.{HttpRequest, ProtocolVersion, RequestMethod, ResponseStatus}
import acsgh.mad.scala.server.router.http.HttpRouterBuilder
import acsgh.mad.scala.server.router.http.convertions.HttpDefaultFormats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.reflectiveCalls

class StaticFilesystemFolderFilterTest extends AnyFlatSpec with Matchers with HttpDefaultFormats {

  val baseFolder = "server/core/src/test/resources/assets"

  "StaticFilesystemFolderFilter" should "return 404 if no file" in {
    val router = new HttpRouterBuilder()

    router.filesystemFolder("/assets2", baseFolder)
    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/assets2/home.html"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )
    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_FOUND)
  }

  it should "return 200 if file" in {
    val router = new HttpRouterBuilder()

    router.filesystemFolder("/", baseFolder)
    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map(),
      new Array[Byte](0)
    )
    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.OK)
    new String(response.bodyBytes, "UTF-8") should be("Hi there!")
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.contains("Last-Modified") should be(true)
  }

  it should "return 304 if file not modifier, etag" in {
    val router = new HttpRouterBuilder()

    router.filesystemFolder("/", baseFolder)
    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map("If-None-Match" -> List("396199333EDBF40AD43E62A1C1397793")),
      new Array[Byte](0)
    )
    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_MODIFIED)
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.contains("Last-Modified") should be(true)
  }

  it should "return 304 if file not modifier, date" in {
    val router = new HttpRouterBuilder()

    router.filesystemFolder("/", baseFolder)
    val request = HttpRequest(
      RequestMethod.GET,
      URI.create("/index.html"),
      ProtocolVersion.HTTP_1_1,
      Map("If-Modified-Since" -> List("Mon, 28 Oct 2119 21:30:51 CET")),
      new Array[Byte](0)
    )
    val response = router.build("test", productionMode = false).process(request)

    response.protocolVersion should be(request.protocolVersion)
    response.responseStatus should be(ResponseStatus.NOT_MODIFIED)
    response.headers("ETag") should be(List("396199333EDBF40AD43E62A1C1397793"))
    response.headers.contains("Last-Modified") should be(true)
  }
}
