package acsgh.mad.scala.server.router.http.files

import java.io.{ByteArrayOutputStream, InputStream}
import java.math.BigInteger
import java.net.URLConnection
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.UUID
import java.util.concurrent.TimeUnit

import acsgh.mad.scala.core.http.model.ResponseStatus.{NOT_FOUND, NOT_MODIFIED}
import acsgh.mad.scala.core.http.model.{HttpRequest, RequestMethod}
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model._
import acsgh.mad.scala.server.router.http.params.writer.default._
import org.cache2k.{Cache, Cache2kBuilder}

import scala.concurrent.duration.{Duration, DurationInt}

object FileFilter {
  val DATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
}

abstract class FileFilter(val uri: String, cacheDuration: Duration = 1 minute) extends Route[HttpRouteAction] with HttpDirectives {

  validUrl(uri)


  private val cache: Cache[String, FileInfo] = new Cache2kBuilder[String, FileInfo]() {}
    .name(UUID.randomUUID().toString)
    .expireAfterWrite(cacheDuration.toSeconds, TimeUnit.SECONDS)
    .permitNullValues(true)
    .build

  override val methods: Set[RequestMethod] = Set(RequestMethod.GET)

  override protected def canApply(request: HttpRequest): Boolean = super.canApply(request) && getFileInfo(uri(request)).isDefined

  override val action: HttpRouteAction = { implicit ctx =>
    val requestUri = uri(ctx.request)
    log.trace("Requesting file: {}", requestUri)

    getFileInfoInt(requestUri).fold(error(NOT_FOUND)) { fileInfo =>
      responseHeader("Content-Type", fileInfo.contentType) {
        responseHeader("ETag", fileInfo.etag) {
          responseHeader("Last-Modified", FileFilter.DATE_FORMATTER.format(fileInfo.lastModified)) {

            requestHeader("If-None-Match".opt, "If-Modified-Since".opt) { (ifNoneMatchHeader, ifModifiedSinceHeader) =>
              if (fileInfo.isModified(ifNoneMatchHeader, ifModifiedSinceHeader)) {
                responseBody(fileInfo.content)
              } else {
                error(NOT_MODIFIED)
              }
            }
          }
        }
      }
    }
  }

  private def getFileInfoInt(uri: String): Option[FileInfo] = {
    Option(cache.get(uri)) match {
      case Some(a) => Some(a)
      case None =>
        log.debug("File info not found loading from file: {}", uri)
        getFileInfo(uri) match {
          case Some(a) =>
            cache.put(uri, a)
            Some(a)
          case None =>
            None
        }
    }
  }

  protected def getFileInfo(uri: String): Option[FileInfo]

  protected def addTradingSlash(uri: String): String = if (!uri.startsWith("/")) s"/$uri" else uri

  protected def removeTradingSlash(uri: String): String = if ((uri.length > 1) && uri.startsWith("/")) uri.substring(1) else uri

  protected def removeEndingSlash(uri: String): String = if ((uri.length > 1) && uri.endsWith("/")) uri.substring(0, uri.length - 1) else uri

  protected def calculateEtag(bytes: Array[Byte]): String = {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    val bigInt = new BigInteger(1, digest)
    var result = bigInt.toString(16).toUpperCase

    while (result.length < 32) {
      result = "0" + result
    }
    result
  }

  protected def contentType(filename: String): String = {
    var result = URLConnection.guessContentTypeFromName(filename)
    if (filename.endsWith(".js")) result = "application/javascript"
    else if (filename.endsWith(".css")) result = "text/css"
    if (result == null) result = "text/html"
    result
  }

  protected def bytes(input: InputStream): Array[Byte] = {
    val output = new ByteArrayOutputStream()

    try {
      val buffer = new Array[Byte](1024)

      LazyList.continually(input.read(buffer))
        .takeWhile(_ != -1)
        .foreach(output.write(buffer, 0, _))

      output.toByteArray
    } finally {
      output.close()
    }
  }

  private def uri(request: HttpRequest): String = {
    val params = extractPathParams(uri, request.uri)
    params.get("path").map(addTradingSlash).map(removeEndingSlash).getOrElse(request.path)
  }
}
