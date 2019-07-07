package com.acsgh.scala.mad.converter.template.thymeleaf

import java.io.{ByteArrayOutputStream, PrintWriter}

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.model.Response
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

trait ThymeleafDirectives {
  directives: Directives =>

  implicit protected def toContext(map: Map[String, Any]): Context = {
    val context = new Context
    map.foreach(e => context.setVariable(e._1, e._2))
    context
  }

  def thymeleafTemplate(templateName: String, params: Map[String, String])(implicit context: RequestContext, thymeleafEngine: TemplateEngine): Response = {
    val out = new ByteArrayOutputStream
    try {
      val printStream = new PrintWriter(out)
      thymeleafEngine.process(templateName, params, printStream)
      responseBody(out.toByteArray)
    } catch {
      case e: Exception =>
        throw new RuntimeException(e)
    } finally {
      if (out != null) out.close()
    }
  }
}
