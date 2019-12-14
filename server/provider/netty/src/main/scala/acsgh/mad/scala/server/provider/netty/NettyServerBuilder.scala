package acsgh.mad.scala.server.provider.netty

import acsgh.mad.scala.server.{Server, ServerBuilder}

class NettyServerBuilder() extends ServerBuilder {

  override def build(): Server = NettyServer(
    _name,
    _productionMode,
    _ipAddress,
    _httpPort,
    _httpsPort,
    _sslConfig,
    _readerIdleTimeSeconds,
    _writerIdleTimeSeconds,
    httpRouterBuilder.build(_name, _productionMode),
    wsRouterBuilder.build(_name, _productionMode)
  )
}
