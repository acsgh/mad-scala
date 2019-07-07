package com.acs.scala.server.mad.converter.template.freemarker

import java.io.{ByteArrayOutputStream, PrintWriter}

import com.acs.scala.server.mad.router.http.RequestContext
import com.acs.scala.server.mad.router.http.directives.Directives
import com.acs.scala.server.mad.router.http.model.Response

trait FreemarkerDirectives {
  directives: Directives =>

  protected val freemarkerHttpServer: FreemarkerHttpServer

  def freemarkerTemplate(templateName: String, params: Map[String, String])(implicit context: RequestContext): Response = {
    val out = new ByteArrayOutputStream
    val printStream = new PrintWriter(out)
    try {
      val template = freemarkerHttpServer.freemarkerConfig.getTemplate(templateName, "UTF-8")
      template.process(params, printStream)
      responseBody(out.toByteArray)
    } catch {
      case e: Exception =>
        throw new RuntimeException(e)
    } finally {
      if (out != null) out.close()
      if (printStream != null) printStream.close()
    }
  }
}
