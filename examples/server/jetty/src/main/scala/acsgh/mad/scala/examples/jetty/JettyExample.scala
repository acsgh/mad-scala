package acsgh.mad.scala.examples.jetty

import acsgh.mad.scala.server.converter.json.spray.{JsonErrorCodeHandler, JsonExceptionHandler}
import acsgh.mad.scala.server.converter.template.thymeleaf.ThymeleafEngineProvider
import acsgh.mad.scala.server.provider.jetty.JettyServerApp
import acsgh.mad.scala.server.router.http.listener.LoggingEventListener
import acsgh.mad.scala.server.router.ws.listener.WSLoggingEventListener
import acsgh.mad.scala.server.{Server, ServerBuilder}
import org.thymeleaf.TemplateEngine

object JettyExample extends JettyServerApp {

  override val name: String = "Jetty Boot Example"

  override protected def buildServer(builder: ServerBuilder): Server = {
    builder.http.addRequestListeners(LoggingEventListener)
    builder.ws.addRequestListeners(WSLoggingEventListener)

    builder.http.resourceFolder("/", "public")
    builder.http.webjars()
    builder.http.defaultErrorCodeHandler(new JsonErrorCodeHandler())
    builder.http.exceptionHandler(new JsonExceptionHandler())

    implicit val thymeleafEngine: TemplateEngine = ThymeleafEngineProvider.build("/templates/")

    PersonRoutes(builder)
    FormsRoutes(builder)

    builder.build()
  }
}
