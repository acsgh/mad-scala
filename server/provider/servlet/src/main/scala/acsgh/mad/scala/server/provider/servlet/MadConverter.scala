package acsgh.mad.scala.server.provider.servlet


import java.net.URI
import java.util

import acsgh.mad.scala.core.http.model._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import scala.io.Source

trait MadConverter {
  protected def transferParams(madResponse: HttpResponse, response: HttpServletResponse): Unit = {
    response.setStatus(madResponse.responseStatus.code)
    madResponse.headers.foreach(header => header._2.foreach(value => response.addHeader(header._1, value)))

    if ((madResponse.bodyBytes != null) && (madResponse.bodyBytes.nonEmpty)) {
      response.getOutputStream.write(madResponse.bodyBytes)
    }
  }

  protected def toMadRequest(request: HttpServletRequest): HttpRequest = HttpRequest(
    getMethod(request.getMethod),
    getUri(request),
    getProtocol(request.getProtocol),
    getHeaders(request),
    getBody(request),
    Some(request.getRemoteAddr)
  )

  private def getUri(request: HttpServletRequest): URI = {
    var result = request.getRequestURI

    if (request.getQueryString != null) {
      result += "?" + request.getQueryString
    }

    URI.create(result)
  }

  private def getBody(request: HttpServletRequest): Array[Byte] = Source.fromInputStream(request.getInputStream, "UTF-8").mkString.getBytes("UTF-8")

  private def getHeaders(request: HttpServletRequest) =
    toList(request.getHeaderNames).map(name => (name, toList(request.getHeaders(name)))).toMap


  private def toList[T](values: util.Enumeration[T]): List[T] = {
    var result = List[T]()
    while (values.hasMoreElements) {
      result = result ++ List(values.nextElement)
    }
    result
  }

  private def getProtocol(protocol: String) = ProtocolVersion.withName(protocol)

  private def getMethod(method: String) = RequestMethod.withName(method)

}

