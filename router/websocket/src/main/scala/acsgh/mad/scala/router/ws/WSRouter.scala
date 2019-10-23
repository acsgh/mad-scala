package acsgh.mad.scala.router.ws

import acsgh.mad.scala.router.ws.handler.{DefaultHandler, WSHandler}
import acsgh.mad.scala.router.ws.model.{WSRequest, WSResponse, WSResponseBuilder}
import com.acsgh.common.scala.log.{LogLevel, LogSupport}
import com.acsgh.common.scala.time.StopWatch

case class WSRoute
(
  subprotocols: Set[String],
  handler: WSHandler
)

case class WSRequestContext
(
  request: WSRequest,
  response: WSResponseBuilder,
  route: Option[WSRoute] = None
)

trait WSRouter extends LogSupport {

  private[mad] var wsRoutes: Map[String, WSRoute] = Map()
  protected val defaultHandler: WSHandler = new DefaultHandler()

  private[ws] def route(uri: String, subprotocols: Set[String] = Set())(handler: WSHandler): Unit = wsRoutes = wsRoutes + (uri -> WSRoute(subprotocols, handler))

  def process(request: WSRequest): Option[WSResponse] = {
    implicit val context: WSRequestContext = WSRequestContext(request, WSResponseBuilder(request))
    log.trace("WS Request {} {}", Array(request.uri): _*)
    val stopWatch = StopWatch.createStarted()
    try {
      wsRoutes.get(request.uri.toString).map(_.handler).getOrElse(defaultHandler).handle
    } catch {
      case e: Exception =>
        log.error("Error during request", e)
        throw e
    } finally {
      stopWatch.printElapseTime("Request " + request.uri, log, LogLevel.DEBUG)
    }
  }
}
