package acsgh.mad.scala.server.provider.jetty.internal

import java.util.concurrent.atomic.AtomicBoolean

import acsgh.mad.scala.server.router.http.HttpRouter
import acsgh.mad.scala.server.router.ws.WSRouter
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{Channel, ChannelOption, EventLoopGroup}
import io.netty.handler.logging.{LogLevel, LoggingHandler}
import io.netty.handler.ssl.SslContext

private[jetty] class NettyServer
(
  private val host: String,
  private val port: Int,
  private val sslContext: Option[SslContext],
  private val httpRouter: HttpRouter,
  private val wsRouter: WSRouter,
  private val workerThreads: Int,
  private val readerIdleTimeSeconds: Int,
  private val writerIdleTimeSeconds: Int
) {
  private val started = new AtomicBoolean(false)

  private var bossGroup: EventLoopGroup = _
  private var workerGroup: EventLoopGroup = _
  private var channel: Channel = _

  private[scala] def start(): Unit = {
    if (started.compareAndSet(false, true)) {
      bossGroup = new NioEventLoopGroup(1)
      workerGroup = new NioEventLoopGroup(workerThreads)
      val b = new ServerBootstrap
      b.option[Integer](ChannelOption.SO_BACKLOG, 1024)
      b.group(bossGroup, workerGroup)
        .channel(classOf[NioServerSocketChannel])
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .childHandler(new NettyServerChannelInitializer(httpRouter, wsRouter, sslContext, readerIdleTimeSeconds, writerIdleTimeSeconds))

      channel = b.bind(host, port).sync.channel
    }
  }

  private[scala] def stop(): Unit = {
    if (started.compareAndSet(true, false)) {
      channel.close.sync
      close(bossGroup)
      close(workerGroup)
    }
  }

  private def close(eventLoopGroup: EventLoopGroup): Unit = {
    if (eventLoopGroup != null) {
      eventLoopGroup.shutdownGracefully
    }
  }
}
