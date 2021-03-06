package acsgh.mad.scala.server.provider.netty.internal

import acsgh.mad.scala.server.router.http.HttpRouter
import acsgh.mad.scala.server.router.ws.WSRouter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpServerCodec}
import io.netty.handler.ssl.SslContext
import io.netty.handler.timeout.IdleStateHandler

private[netty] class NettyServerChannelInitializer
(
  private val httpRouter: HttpRouter,
  private val wsRouter: WSRouter,
  private val sslContext: Option[SslContext],
  private val readerIdleTimeSeconds: Int,
  private val writerIdleTimeSeconds: Int,
) extends ChannelInitializer[SocketChannel] {

  override def initChannel(ch: SocketChannel): Unit = {
    val p = ch.pipeline

    sslContext.foreach(ctx => p.addLast(ctx.newHandler(ch.alloc())))
    p.addLast(new HttpServerCodec)
    p.addLast(new HttpObjectAggregator(64 * 1024 * 1024))
    p.addLast("idleStateHandler", new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, 0))

    for (route <- wsRouter.wsRoutes) {
      if (route._2.subprotocols.isEmpty) {
        p.addLast(new NettyServerChannelWebSocketHandler(wsRouter, route._1, None))
      } else {
        for (subprotocol <- route._2.subprotocols) {
          p.addLast(new NettyServerChannelWebSocketHandler(wsRouter, route._1, Some(subprotocol)))
        }
      }
    }

    p.addLast(new NettyServerChannelHandler(httpRouter))
  }
}
