package acsgh.mad.scala.provider.jetty

import java.net.URI
import java.nio.ByteBuffer
import java.util.UUID

import acsgh.mad.scala.router.ws.model._
import acsgh.mad.scala.router.ws.{WSRoute, WSRouter}
import org.eclipse.jetty.http.pathmap.ServletPathSpec
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.websocket.api.{Session, WebSocketAdapter}
import org.eclipse.jetty.websocket.server.{NativeWebSocketConfiguration, WebSocketUpgradeFilter}
import org.eclipse.jetty.websocket.servlet.{ServletUpgradeRequest, ServletUpgradeResponse, WebSocketCreator}

import scala.jdk.CollectionConverters._

object JettyWsHandler  {

  def init(wsRouter: WSRouter, webSocketIdleTimeoutMillis: Option[Int]): ServletContextHandler = {
    val result = new ServletContextHandler(null, "/", true, false)
    val webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(result)
    webSocketIdleTimeoutMillis.map(_.toLong).foreach(webSocketUpgradeFilter.getFactory.getPolicy.setIdleTimeout)

    val webSocketConfiguration = result.getServletContext.getAttribute(classOf[NativeWebSocketConfiguration].getName).asInstanceOf[NativeWebSocketConfiguration]

    wsRouter.wsRoutes.foreach { e =>
      val creator = new JettyWebSocketCreator(wsRouter, e._1, e._2)
      webSocketConfiguration.addMapping(new ServletPathSpec(e._1), creator)
    }

    result
  }
}

class JettyWebSocketCreator(private var wsRouter: WSRouter, private val uri: String, private val route: WSRoute) extends WebSocketCreator {

  override def createWebSocket(req: ServletUpgradeRequest, resp: ServletUpgradeResponse): AnyRef = {
    val requestSubProtocols = req.getSubProtocols.asScala.toSet
    val subProtocols = if (route.subprotocols.isEmpty) requestSubProtocols else requestSubProtocols.intersect(route.subprotocols)

    for (subProtocol <- subProtocols) {
      resp.setAcceptedSubProtocol(subProtocol)
    }

    new JettyWebSocketAdapter(wsRouter, uri, route)
  }
}

class JettyWebSocketAdapter(private var wsRouter: WSRouter, private val uri: String, private val route: WSRoute) extends WebSocketAdapter {
  private val id = UUID.randomUUID().toString

  override def onWebSocketBinary(payload: Array[Byte], offset: Int, len: Int): Unit = {
    super.onWebSocketBinary(payload, offset, len)

    val data = new Array[Byte](len)
    System.arraycopy(payload, offset, data, 0, len)

    val request = WSRequestBinary(
      id,
      getSession.getRemoteAddress.toString,
      URI.create(uri),
      data
    )

    handleRequest(request)
  }

  override def onWebSocketClose(statusCode: Int, reason: String): Unit = {
    super.onWebSocketClose(statusCode, reason)

    val request = WSRequestDisconnect(
      id,
      getSession.getRemoteAddress.toString,
      URI.create(uri)
    )

    handleRequest(request)
  }

  override def onWebSocketConnect(sess: Session): Unit = {
    super.onWebSocketConnect(sess)

    val request = WSRequestConnect(
      id,
      getSession.getRemoteAddress.toString,
      URI.create(uri)
    )

    handleRequest(request)
  }

  override def onWebSocketText(message: String): Unit = {
    val request = WSRequestText(
      id,
      getSession.getRemoteAddress.toString,
      URI.create(uri),
      message
    )

    handleRequest(request)
  }

  def handleRequest(request: WSRequest): Unit = {
    val response = wsRouter.process(request)

    response.foreach {
      case m: WSResponseConnect =>
        if (m.close) {
          getSession.close()
        }
      case m: WSResponseDisconnect =>
        if (m.close) {
          getSession.close()
        }
      case m: WSResponseText =>
        getRemote.sendString(m.text)
      case m: WSResponseBinary =>
        getRemote.sendBytes(ByteBuffer.wrap(m.bytes))
    }
  }
}