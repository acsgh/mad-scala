package com.acsgh.scala.mad.provider.netty

import com.acsgh.scala.mad.router.http.HttpRouter
import com.acsgh.scala.mad.router.ws.WSRouter
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
      p.addLast(new NettyWebSocketFrameHandler(wsRouter, route._1.uri, route._1.subprotocol))
    }

    p.addLast(new NettyServerChannelHandler(httpRouter))
  }
}
