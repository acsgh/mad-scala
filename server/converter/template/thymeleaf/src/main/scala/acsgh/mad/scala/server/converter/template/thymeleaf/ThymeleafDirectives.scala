package acsgh.mad.scala.server.converter.template.thymeleaf

import acsgh.mad.scala.server.router.http.body.writer._
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

import scala.language.implicitConversions

trait ThymeleafDirectives {
  directives: HttpDirectives =>

  protected val thymeleafEngine: TemplateEngine

  private val htmlCompressorFilter: HtmlCompressor = {
    val c = new HtmlCompressor()
    c.setPreserveLineBreaks(false)
    c.setRemoveComments(true)
    c.setRemoveIntertagSpaces(true)
    c.setRemoveHttpProtocol(true)
    c.setRemoveHttpsProtocol(true)
    c
  }

  implicit protected def toContext(map: Map[String, Any]): Context = {
    val context = new Context
    map.foreach(e => context.setVariable(e._1, e._2))
    context
  }

  implicit object ThymeleafBodyWriter extends HttpBodyWriter[ThymeleafTemplate] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(input: ThymeleafTemplate)(implicit context: HttpRequestContext): Array[Byte] = {
      val body = thymeleafEngine.process(input.templateName, input.params)
      val finalBody = if (context.router.productionMode) htmlCompressorFilter.compress(body) else body
      finalBody.getBytes("UTF-8")
    }
  }

}
