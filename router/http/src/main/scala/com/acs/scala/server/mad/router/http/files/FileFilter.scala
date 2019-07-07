package com.acs.scala.server.mad.router.http.files

import java.io.InputStream
import java.math.BigInteger
import java.net.URLConnection
import java.security.MessageDigest
import java.text.SimpleDateFormat

import com.acs.scala.server.mad.router.http.model.{Response, ResponseStatus}
import com.acs.scala.server.mad.router.http.{RequestContext, RequestFilter}

import scala.io.Source


object FileFilter {
  val DATE_FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
}

abstract class FileFilter extends RequestFilter {

  override def handle(nextJump: () => Response)(implicit context: RequestContext): Response = {
    val requestUri = uri(context)
    log.trace("Requesting file: {}", requestUri)

    getFileInfo(requestUri).fold(nextJump()) { fileInfo =>
      responseHeader("Content-Type", fileInfo.contentType) {
        responseHeader("ETag", fileInfo.etag) {
          responseHeader("Last-Modified", FileFilter.DATE_FORMATTER.format(fileInfo.lastModified)) {

            requestHeader("If-None-Match".opt, "If-Modified-Since".opt) { (ifNoneMatchHeader, ifModifiedSinceHeader) =>
              if (fileInfo.isModified(ifNoneMatchHeader, ifModifiedSinceHeader)) {
                responseBody(fileInfo.content)
              } else {
                error(ResponseStatus.NOT_MODIFIED)
              }
            }
          }
        }
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

  protected def bytes(input: InputStream): Array[Byte] = Source.fromInputStream(input, "UTF-8").mkString.getBytes("UTF-8")


  private def uri(context: RequestContext) = context.pathParams.get("path").map(addTradingSlash).map(removeEndingSlash).getOrElse(context.request.path)
}