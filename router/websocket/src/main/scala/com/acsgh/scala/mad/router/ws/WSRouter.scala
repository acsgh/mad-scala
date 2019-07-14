package com.acsgh.scala.mad.router.ws

import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch
import com.acsgh.scala.mad.router.ws.handler.{DefaultHandler, WSHandler}
import com.acsgh.scala.mad.router.ws.model.{WSRequest, WSResponse, WSResponseBuilder}

private[router] case class WSRoute
(
  uri: String,
  subprotocol: Option[String]
)

case class WSRequestContext
(
  request: WSRequest,
  response: WSResponseBuilder,
  route: Option[WSRoute] = None
)

trait WSRouter extends LogSupport {

  private[mad] var wsRoutes: Map[WSRoute, WSHandler] = Map()
  protected val defaultHandler: WSHandler = new DefaultHandler()

  private[ws] def route(route: WSRoute)(handler: WSHandler): Unit = wsRoutes = wsRoutes + (route -> handler)

  def process(request: WSRequest): Option[WSResponse] = {
    implicit val context: WSRequestContext = WSRequestContext(request, WSResponseBuilder(request))
    log.trace("WS Request {} {}", Array(request.uri, request.subprotocol): _*)
    val stopWatch = StopWatch.createStarted()
    try {
      wsRoutes.getOrElse(WSRoute(request.uri.toString, request.subprotocol), defaultHandler).handle
    } catch {
      case e: Exception =>
        log.error("Error during request", e)
        throw e
    } finally {
      stopWatch.printElapseTime("Request " + request.uri + " " + request.subprotocol, log, LogLevel.DEBUG)
    }
  }
}
