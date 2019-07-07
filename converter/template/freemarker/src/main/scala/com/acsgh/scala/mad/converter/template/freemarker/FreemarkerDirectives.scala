package com.acsgh.scala.mad.converter.template.freemarker

import java.io.{ByteArrayOutputStream, PrintWriter}

import com.acsgh.scala.mad.router.http.RequestContext
import com.acsgh.scala.mad.router.http.directives.Directives
import com.acsgh.scala.mad.router.http.model.Response
import freemarker.template.Configuration

trait FreemarkerDirectives {
  directives: Directives =>

  def freemarkerTemplate(templateName: String, params: Map[String, String])(implicit context: RequestContext, freemarkerConfig: Configuration): Response = {
    val out = new ByteArrayOutputStream
    val printStream = new PrintWriter(out)
    try {
      val template = freemarkerConfig.getTemplate(templateName, "UTF-8")
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
