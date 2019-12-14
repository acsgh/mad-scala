package acsgh.mad.scala.server.provider.netty.internal

import java.net.URI
import java.util

import acsgh.mad.scala.core.ws.model._
import acsgh.mad.scala.server.router.ws.WSRouter
import com.acsgh.common.scala.log.LogSupport
import io.netty.buffer.{ByteBufUtil, Unpooled}
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.{BinaryWebSocketFrame, TextWebSocketFrame, WebSocketFrame, WebSocketServerProtocolHandler}

private[netty] class NettyServerChannelWebSocketHandler
(
  private val wsRouter: WSRouter,
  private val uri: String,
  private val subprotocol: Option[String]
) extends WebSocketServerProtocolHandler(uri, subprotocol.orNull, true) with LogSupport {

  private var connected: Boolean = false

  override def channelUnregistered(ctx: ChannelHandlerContext): Unit = {
    super.channelUnregistered(ctx)

    if (connected) {
      wsRouter.process(toWebSocketDisconnectedRequest(ctx)).foreach { response =>
        if (response.close) {
          ctx.close
        }
      }
      connected = false
    }
  }

  override protected def decode(ctx: ChannelHandlerContext, frame: WebSocketFrame, out: util.List[AnyRef]): Unit = {
    if (!connected) {
      wsRouter.process(toWebSocketConnectedRequest(ctx)).foreach { response =>
        if (response.close) {
          ctx.close
        }
      }
      connected = true
    }
    getWebSocketRequest(ctx, frame).fold(super.decode(ctx, frame, out)) { r =>
      wsRouter.process(r).foreach { response =>
        toWebSocketFrame(response).foreach(ctx.channel.writeAndFlush)

        if (response.close) {
          ctx.close
        }
      }
    }
  }

  private def toWebSocketFrame(response: WSResponse): Option[WebSocketFrame] = response match {
    case r: WSResponseText => Some(new TextWebSocketFrame(r.text))
    case r: WSResponseBinary => Some(new BinaryWebSocketFrame(Unpooled.wrappedBuffer(r.bytes)))
    case _ => None
  }

  private def getWebSocketRequest(ctx: ChannelHandlerContext, frame: WebSocketFrame): Option[WSRequest] = frame match {
    case f: TextWebSocketFrame => Some(getTextRequest(ctx, f))
    case f: BinaryWebSocketFrame => Some(getBinaryRequest(ctx, f))
    case _ => None
  }

  private def toWebSocketConnectedRequest(ctx: ChannelHandlerContext) = WSRequestConnect(
    ctx.channel.remoteAddress.toString,
    URI.create(uri)
  )

  private def toWebSocketDisconnectedRequest(ctx: ChannelHandlerContext) = WSRequestDisconnect(
    ctx.channel.remoteAddress.toString,
    URI.create(uri)
  )

  private def getTextRequest(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) = WSRequestText(
    ctx.channel.remoteAddress.toString,
    URI.create(uri),
    frame.text()
  )

  private def getBinaryRequest(ctx: ChannelHandlerContext, frame: BinaryWebSocketFrame) = WSRequestBinary(
    ctx.channel.remoteAddress.toString,
    URI.create(uri),
    ByteBufUtil.getBytes(frame.content)
  )
}
