package acsgh.mad.scala.router.http.files

import java.io.{ByteArrayOutputStream, InputStream}
import java.math.BigInteger
import java.net.URLConnection
import java.security.MessageDigest
import java.text.SimpleDateFormat

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.model.ResponseStatus.NOT_MODIFIED
import acsgh.mad.scala.router.http.model._
import acsgh.mad.scala.router.http.directives.Directives


object FileFilter {
  val DATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
}

abstract class FileFilter extends Directives {

  def handle(context: RequestContext): Response = {
    implicit val ctx = context
    val requestUri = uri(context)
    log.trace("Requesting file: {}", requestUri)
    error(NOT_MODIFIED)
//    getFileInfo(requestUri).fold(Left[NotHandled]) { fileInfo =>
//      responseHeader("Content-Type", fileInfo.contentType) {
//        responseHeader("ETag", fileInfo.etag) {
//          responseHeader("Last-Modified", FileFilter.DATE_FORMATTER.format(fileInfo.lastModified)) {
//
//            requestHeader("If-None-Match".opt, "If-Modified-Since".opt) { (ifNoneMatchHeader, ifModifiedSinceHeader) =>
//              if (fileInfo.isModified(ifNoneMatchHeader, ifModifiedSinceHeader)) {
//                responseBody(fileInfo.content)
//              } else {
//                error(NOT_MODIFIED)
//              }
//            }
//          }
//        }
//      }
//    }
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


  private def uri(context: RequestContext) = context.pathParams.get("path").map(addTradingSlash).map(removeEndingSlash).getOrElse(context.request.path)
}
