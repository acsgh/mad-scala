package acsgh.mad.scala.provider.netty

import java.net.{InetSocketAddress, SocketAddress, URI}

import acsgh.mad.scala.router.http.HttpRouter
import acsgh.mad.scala.router.http.model.{ProtocolVersion, Request, RequestMethod, Response}
import com.acsgh.common.scala.log.LogSupport
import io.netty.buffer.{ByteBufUtil, Unpooled}
import io.netty.channel.{ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.handler.codec.http.HttpHeaderNames.{CONNECTION, CONTENT_LENGTH}
import io.netty.handler.codec.http.HttpResponseStatus.CONTINUE
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import io.netty.handler.codec.http._

import scala.jdk.CollectionConverters._

class NettyServerChannelHandler(private val httpRouter: HttpRouter) extends ChannelInboundHandlerAdapter with LogSupport {

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = ctx.flush

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = msg match {
    case req: HttpRequest =>
      if (HttpUtil.isKeepAlive(req)) ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE))
      val keepAlive = HttpUtil.isKeepAlive(req)
      val madRequest = geHttpRequest(req, ctx.channel.remoteAddress)
      val wareResponse = httpRouter.process(madRequest)
      val response = getNettyResponse(wareResponse)
      if (!keepAlive) ctx.write(response).addListener(ChannelFutureListener.CLOSE)
      else {
        response.headers.set(CONNECTION, HttpHeaderValues.KEEP_ALIVE)
        ctx.write(response)
      }
  }

  private def getNettyResponse(response: Response) = {
    val nettyResponse = new DefaultFullHttpResponse(
      getNettyHttpVersion(response.protocolVersion),
      HttpResponseStatus.valueOf(response.responseStatus.code),
      Unpooled.wrappedBuffer(response.bodyBytes)
    )

    response.headers.foreach(h => nettyResponse.headers.set(h._1, h._2.asJava))
    nettyResponse.headers.set(CONTENT_LENGTH, nettyResponse.content.readableBytes)
    nettyResponse
  }

  private def geHttpRequest(request: HttpRequest, socketAddress: SocketAddress) = Request(
    getRequestMethod(request.method),
    socketAddress.asInstanceOf[InetSocketAddress].getHostName,
    URI.create(request.uri),
    getHTTPVersion(request.protocolVersion),
    getHeaders(request),
    getBody(request)
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
        ByteBufUtil.getBytes(content)
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
