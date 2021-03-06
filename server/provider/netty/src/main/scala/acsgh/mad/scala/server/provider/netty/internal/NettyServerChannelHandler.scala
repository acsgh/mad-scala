package acsgh.mad.scala.server.provider.netty.internal

import java.net.{InetSocketAddress, SocketAddress, URI}

import acsgh.mad.scala.core.http.model.{ProtocolVersion, RequestMethod, HttpRequest => MadRequest, HttpResponse => MadResponse}
import acsgh.mad.scala.server.router.http.HttpRouter
import com.acsgh.common.scala.log.LogSupport
import io.netty.buffer.{ByteBufUtil, Unpooled}
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.handler.codec.http.HttpHeaderNames.{CONNECTION, CONTENT_LENGTH}
import io.netty.handler.codec.http.HttpResponseStatus.CONTINUE
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import io.netty.handler.codec.http._
import io.netty.util.ReferenceCountUtil

import scala.jdk.CollectionConverters._

private[netty] class NettyServerChannelHandler
(
  private val httpRouter: HttpRouter
) extends ChannelInboundHandlerAdapter with LogSupport {

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = ctx.flush

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    try {
      msg match {
        case req: HttpRequest =>
          if (HttpUtil.isKeepAlive(req)) ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE))
          val keepAlive = HttpUtil.isKeepAlive(req)

          val madRequest = geHttpRequest(req, ctx.channel.remoteAddress)
          val madResponse = httpRouter.process(madRequest)

          val response = getNettyResponse(madResponse)
          if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE)
          }
          else {
            response.headers.set(CONNECTION, HttpHeaderValues.KEEP_ALIVE)
            ctx.write(response)
          }
      }
    } finally {
      try {
        ReferenceCountUtil.release(msg);
      } catch {
        case e: Exception => log.trace("Unable to release connection", e)
      }
    }
  }

  private def getNettyResponse(response: MadResponse) = {
    val nettyResponse = new DefaultFullHttpResponse(
      getNettyHttpVersion(response.protocolVersion),
      HttpResponseStatus.valueOf(response.responseStatus.code),
      Unpooled.wrappedBuffer(response.bodyBytes)
    )

    response.headers.foreach(h => nettyResponse.headers.set(h._1, h._2.asJava))
    nettyResponse.headers.set(CONTENT_LENGTH, nettyResponse.content.readableBytes)
    nettyResponse
  }

  private def geHttpRequest(request: HttpRequest, socketAddress: SocketAddress) = MadRequest(
    getRequestMethod(request.method),
    URI.create(request.uri),
    getHTTPVersion(request.protocolVersion),
    getHeaders(request),
    getBody(request),
    Some(socketAddress.asInstanceOf[InetSocketAddress].getHostName)
  )

  private def getHeaders(request: HttpRequest): Map[String, List[String]] = {
    request.headers.asScala.map(e => (e.getKey, e.getValue))
      .groupBy(_._1)
      .view
      .mapValues(_.map(_._2).toList)
      .toMap
  }

  private def getNettyHttpVersion(protocolVersion: ProtocolVersion) = HttpVersion.valueOf(protocolVersion.entryName)

  private def getHTTPVersion(httpVersion: HttpVersion) = ProtocolVersion.withNameInsensitive(httpVersion.toString)

  private def getRequestMethod(method: HttpMethod) = RequestMethod.withNameInsensitive(method.name)

  private def getBody(request: HttpRequest): Array[Byte] = request match {
    case c: HttpContent =>
      val content = c.content
      if (content.isReadable) {
        val result = ByteBufUtil.getBytes(content)
        content.release()
        result
      } else {
        new Array[Byte](0)
      }
    case _ => new Array[Byte](0)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    log.error("Error during request", cause)
    ctx.close
  }

}
