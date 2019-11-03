package acsgh.mad.scala.provider

import java.net.URI
import java.util

import acsgh.mad.scala.router.ws.WSRouter
import acsgh.mad.scala.router.ws.model._
import com.acsgh.common.scala.log.LogSupport
import io.netty.buffer.{ByteBufUtil, Unpooled}
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.{BinaryWebSocketFrame, TextWebSocketFrame, WebSocketFrame, WebSocketServerProtocolHandler}

class NettyServerChannelWebServiceHandler
(
  private val wsRouter: WSRouter,
  private val uri: String,
  private val subprotocol: Option[String]
) extends WebSocketServerProtocolHandler(uri, subprotocol.orNull, true) with LogSupport {

  override def channelRegistered(ctx: ChannelHandlerContext): Unit = {
    super.channelRegistered(ctx)

    wsRouter.process(toWebSocketConnectedRequest(ctx)).foreach { response =>
      if (response.close) {
        ctx.close
      }
    }
  }

  override def channelUnregistered(ctx: ChannelHandlerContext): Unit = {
    super.channelUnregistered(ctx)

    wsRouter.process(toWebSocketDisconnectedRequest(ctx)).foreach { response =>
      if (response.close) {
        ctx.close
      }
    }
  }

  override protected def decode(ctx: ChannelHandlerContext, frame: WebSocketFrame, out: util.List[AnyRef]): Unit = {
    getWebSocketRequest(ctx, frame).fold(super.decode(ctx, frame, out)) { r =>
      wsRouter.process(r).foreach { response =>
        toWebSocketFrame(response).foreach(ctx.channel.writeAndFlush)
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
    ctx.channel.id.asLongText,
    ctx.channel.remoteAddress.toString,
    URI.create(uri)
  )

  private def toWebSocketDisconnectedRequest(ctx: ChannelHandlerContext) = WSRequestDisconnect(
    ctx.channel.id.asLongText,
    ctx.channel.remoteAddress.toString,
    URI.create(uri)
  )

  private def getTextRequest(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) = WSRequestText(
    ctx.channel.id.asLongText,
    ctx.channel.remoteAddress.toString,
    URI.create(uri),
    frame.text()
  )

  private def getBinaryRequest(ctx: ChannelHandlerContext, frame: BinaryWebSocketFrame) = WSRequestBinary(
    ctx.channel.id.asLongText,
    ctx.channel.remoteAddress.toString,
    URI.create(uri),
    ByteBufUtil.getBytes(frame.content)
  )
}
