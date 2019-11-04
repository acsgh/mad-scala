package acsgh.mad.scala.converter.template.thymeleaf

import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.{ClassLoaderTemplateResolver, FileTemplateResolver}

object ThymeleafEngineProvider {

  def build(prefix: String, suffix: String = ".html", source: ThymeleafSource = ThymeleafSource.Classpath, templateMode: TemplateMode = TemplateMode.HTML, encoding: String = "UTF-8"): TemplateEngine = {
    val engine = new TemplateEngine

    try {
      val templateResolver = source match {
        case ThymeleafSource.File =>
          new FileTemplateResolver
        case ThymeleafSource.Classpath =>
          new ClassLoaderTemplateResolver
        case _ =>
          throw new NullPointerException("Source " + source + " is not valid")
      }
      templateResolver.setTemplateMode(templateMode)
      templateResolver.setPrefix(prefix)
      templateResolver.setSuffix(suffix)
      templateResolver.setCharacterEncoding(encoding)
      engine.setTemplateResolver(templateResolver)
    } catch {
      case e: Exception =>
        throw new RuntimeException(e)
    }

    engine
  }
}
