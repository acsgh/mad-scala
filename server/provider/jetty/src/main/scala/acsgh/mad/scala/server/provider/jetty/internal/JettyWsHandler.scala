package acsgh.mad.scala.server.provider.jetty.internal

import java.net.URI
import java.nio.ByteBuffer

import acsgh.mad.scala.core.ws.model._
import acsgh.mad.scala.server.router.ws.WSRouter
import acsgh.mad.scala.server.router.ws.model.WSRoute
import org.eclipse.jetty.http.pathmap.ServletPathSpec
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.websocket.api.{Session, WebSocketAdapter}
import org.eclipse.jetty.websocket.server.{NativeWebSocketConfiguration, WebSocketUpgradeFilter}
import org.eclipse.jetty.websocket.servlet.{ServletUpgradeRequest, ServletUpgradeResponse, WebSocketCreator}

import scala.jdk.CollectionConverters._

private[jetty] object JettyWsHandler {
  def build(wsRouter: WSRouter): ServletContextHandler = {
    val result = new ServletContextHandler(null, "/", true, false)

    val webSocketUpgradeFilter = WebSocketUpgradeFilter.configure(result)
    webSocketUpgradeFilter.getFactory.getPolicy.setIdleTimeout(wsRouter.workerTimeoutSeconds * 1000)

    val webSocketConfiguration = result.getServletContext.getAttribute(classOf[NativeWebSocketConfiguration].getName).asInstanceOf[NativeWebSocketConfiguration]

    wsRouter.wsRoutes.foreach { e =>
      val creator = new JettyWebSocketCreator(wsRouter, e._1, e._2)
      webSocketConfiguration.addMapping(new ServletPathSpec(e._1), creator)
    }

    result
  }
}

private[jetty] class JettyWebSocketCreator
(
  private var wsRouter: WSRouter,
  private val uri: String,
  private val route: WSRoute
) extends WebSocketCreator {


  override def createWebSocket(req: ServletUpgradeRequest, resp: ServletUpgradeResponse): AnyRef = {
    val requestSubProtocols = req.getSubProtocols.asScala.toSet
    val subProtocols = if (route.subprotocols.isEmpty) requestSubProtocols else requestSubProtocols.intersect(route.subprotocols)

    for (subProtocol <- subProtocols) {
      resp.setAcceptedSubProtocol(subProtocol)
    }

    new JettyWebSocketAdapter(wsRouter, uri, route)
  }
}

private[jetty] class JettyWebSocketAdapter
(
  private var wsRouter: WSRouter,
  private val uri: String,
  private val route: WSRoute
) extends WebSocketAdapter {
  override def onWebSocketBinary(payload: Array[Byte], offset: Int, len: Int): Unit = {
    super.onWebSocketBinary(payload, offset, len)

    val data = new Array[Byte](len)
    System.arraycopy(payload, offset, data, 0, len)

    val request = WSRequestBinary(
      getSession.getRemoteAddress.toString,
      URI.create(uri),
      data
    )

    handleRequest(request)
  }

  override def onWebSocketClose(statusCode: Int, reason: String): Unit = {
    super.onWebSocketClose(statusCode, reason)

    val request = WSRequestDisconnect(
      getSession.getRemoteAddress.toString,
      URI.create(uri)
    )

    handleRequest(request)
  }

  override def onWebSocketConnect(sess: Session): Unit = {
    super.onWebSocketConnect(sess)

    val request = WSRequestConnect(
      getSession.getRemoteAddress.toString,
      URI.create(uri)
    )

    handleRequest(request)
  }

  override def onWebSocketText(message: String): Unit = {
    val request = WSRequestText(
      getSession.getRemoteAddress.toString,
      URI.create(uri),
      message
    )

    handleRequest(request)
  }

  def handleRequest(request: WSRequest): Unit = {
    val response = wsRouter.process(request)

    response.foreach {
      case m: WSResponseText =>
        getRemote.sendString(m.text)

        if (m.close) {
          getSession.close()
        }
      case m: WSResponseBinary =>
        getRemote.sendBytes(ByteBuffer.wrap(m.bytes))

        if (m.close) {
          getSession.close()
        }
      case m: WSResponseClose =>
        if (m.close) {
          getSession.close()
        }
    }
  }
}