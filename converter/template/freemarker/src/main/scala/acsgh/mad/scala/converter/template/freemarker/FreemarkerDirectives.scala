package acsgh.mad.scala.converter.template.freemarker

import java.io.{ByteArrayOutputStream, PrintWriter}

import acsgh.mad.scala.router.http.convertions.BodyWriter
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.model.RequestContext
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import freemarker.template.Configuration

trait FreemarkerDirectives {
  directives: Directives =>

  protected val freemarkerConfig: Configuration

  private val htmlCompressorFilter: HtmlCompressor = {
    val c = new HtmlCompressor()
    c.setPreserveLineBreaks(false)
    c.setRemoveComments(true)
    c.setRemoveIntertagSpaces(true)
    c.setRemoveHttpProtocol(true)
    c.setRemoveHttpsProtocol(true)
    c
  }

  implicit object FreemarkerBodyWriter extends BodyWriter[FreemarkerTemplate] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(input: FreemarkerTemplate)(implicit context: RequestContext): Array[Byte] = {
      val out = new ByteArrayOutputStream
      val printStream = new PrintWriter(out)
      try {
        val template = freemarkerConfig.getTemplate(input.templateName, "UTF-8")
        template.process(input.params, printStream)
        val body = new String(out.toByteArray, "UTF-8")
        val finalBody = if (context.router.productionMode) htmlCompressorFilter.compress(body) else body
        finalBody.getBytes("UTF-8")
      } catch {
        case e: Exception =>
          throw new RuntimeException(e)
      } finally {
        if (out != null) out.close()
        if (printStream != null) printStream.close()
      }
    }
  }

}
