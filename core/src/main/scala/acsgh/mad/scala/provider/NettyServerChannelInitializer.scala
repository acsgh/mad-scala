package acsgh.mad.scala.provider

import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.ws.WSRouter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpServerCodec}
import io.netty.handler.ssl.SslContext

class NettyServerChannelInitializer(private val httpRouter: HttpRouter, private val wsRouter: WSRouter, private val sslContext: Option[SslContext]) extends ChannelInitializer[SocketChannel] {

  override def initChannel(ch: SocketChannel): Unit = {
    val p = ch.pipeline

    sslContext.foreach(ctx => p.addLast(ctx.newHandler(ch.alloc())))
    p.addLast(new HttpServerCodec)
    p.addLast(new HttpObjectAggregator(64 * 1024 * 1024))

    for (route <- wsRouter.wsRoutes) {
      if (route._2.subprotocols.isEmpty) {
        p.addLast(new NettyWebSocketFrameHandler(wsRouter, route._1, None))
      } else {
        for (subprotocol <- route._2.subprotocols) {
          p.addLast(new NettyWebSocketFrameHandler(wsRouter, route._1, Some(subprotocol)))
        }
      }
    }

    p.addLast(new NettyServerChannelHandler(httpRouter))
  }
}
