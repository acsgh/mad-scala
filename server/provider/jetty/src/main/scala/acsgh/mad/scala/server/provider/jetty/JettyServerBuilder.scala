package acsgh.mad.scala.server.provider.jetty

import acsgh.mad.scala.server.{Server, ServerBuilder}

class JettyServerBuilder() extends ServerBuilder {

  override def build(): Server = JettyServer(
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
