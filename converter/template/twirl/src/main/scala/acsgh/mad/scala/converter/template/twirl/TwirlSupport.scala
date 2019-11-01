package acsgh.mad.scala.converter.template.twirl

import acsgh.mad.scala.router.http.RequestContext
import acsgh.mad.scala.router.http.convertions.BodyWriter
import acsgh.mad.scala.router.http.directives.Directives
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import play.twirl.api.HtmlFormat


trait TwirlSupport extends Directives with ProductionInfo {

  private val htmlCompressorFilter: HtmlCompressor = {
    val c = new HtmlCompressor()
    c.setPreserveLineBreaks(false)
    c.setRemoveComments(true)
    c.setRemoveIntertagSpaces(true)
    c.setRemoveHttpProtocol(true)
    c.setRemoveHttpsProtocol(true)
    c
  }

  implicit object TwirlBodyWriter extends BodyWriter[HtmlFormat.Appendable] {
    override val contentType: String = "text/html; charset=UTF-8"

    override def write(input: HtmlFormat.Appendable)(implicit context: RequestContext): Array[Byte] = {
      val body = if (context.router.productionMode) htmlCompressorFilter.compress(input.body) else input.body
      body.getBytes("UTF-8")
    }
  }

}
