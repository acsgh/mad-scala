package acsgh.mad.scala.server.converter.template.twirl

import acsgh.mad.scala.server.router.http.body.writer._
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.HttpRequestContext
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import play.twirl.api.HtmlFormat


trait TwirlSupport extends HttpDirectives {

  protected val htmlCompressorFilter: HtmlCompressor = {
    val c = new HtmlCompressor()
    c.setPreserveLineBreaks(false)
    c.setRemoveComments(true)
    c.setRemoveIntertagSpaces(true)
    c.setRemoveHttpProtocol(true)
    c.setRemoveHttpsProtocol(true)
    c
  }

  implicit object TwirlBodyWriter extends HttpBodyWriter[HtmlFormat.Appendable] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(input: HtmlFormat.Appendable)(implicit context: HttpRequestContext): Array[Byte] = {
      val body = if (context.router.productionMode) htmlCompressorFilter.compress(input.body) else input.body
      body.getBytes("UTF-8")
    }
  }

}
