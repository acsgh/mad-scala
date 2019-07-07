package com.acs.scala.server.mad.converter.template.thymeleaf

import java.io.{ByteArrayOutputStream, PrintWriter}

import com.acs.scala.server.mad.router.RequestContext
import com.acs.scala.server.mad.router.directives.Directives
import com.acs.scala.server.mad.router.model.Response
import org.thymeleaf.context.Context

trait ThymeleafDirectives {
  directives: Directives =>

  protected val thymeleafHttpServer: ThymeleafHttpServer

  implicit protected def toContext(map: Map[String, Any]): Context = {
    val context = new Context
    map.foreach(e => context.setVariable(e._1, e._2))
    context
  }

  def thymeleafTemplate(templateName: String, params: Map[String, String])(implicit context: RequestContext): Response = {
    val out = new ByteArrayOutputStream
    try {
      val printStream = new PrintWriter(out)
      thymeleafHttpServer.thymeleafEngine.process(templateName, params, printStream)
      responseBody(out.toByteArray)
    } catch {
      case e: Exception =>
        throw new RuntimeException(e)
    } finally {
      if (out != null) out.close()
    }
  }
}
