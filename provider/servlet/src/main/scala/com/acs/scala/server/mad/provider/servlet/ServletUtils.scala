package com.acs.scala.server.mad.provider.servlet


import java.net.URI
import java.util

import com.acs.scala.server.mad.router.http.model._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import scala.io.Source

object ServletUtils {
  def transferParams(waveResponse: Response, response: HttpServletResponse): Unit = {
    waveResponse.headers.foreach(header => header._2.foreach(value => response.addHeader(header._1, value)))
    response.getOutputStream.write(waveResponse.bodyBytes)
    response.setStatus(waveResponse.responseStatus.code)
  }

  def toWaveRequest(request: HttpServletRequest) = Request(
    getMethod(request.getMethod),
    request.getRemoteAddr,
    getUri(request),
    getProtocol(request.getProtocol),
    getHeaders(request),
    getBody(request)
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
