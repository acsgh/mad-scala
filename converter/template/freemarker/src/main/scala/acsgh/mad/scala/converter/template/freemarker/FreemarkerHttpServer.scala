package acsgh.mad.scala.converter.template.freemarker

import java.io.File

import acsgh.mad.scala.router.http.HttpServer
import freemarker.cache.ClassTemplateLoader
import freemarker.template.{Configuration, TemplateExceptionHandler}

trait FreemarkerHttpServer extends FreemarkerDirectives {
  server: HttpServer =>

  protected val templateFolder: File
  protected val classpathFolder: String
  protected val encoding: String = "UTF-8"
  protected val templateExceptionHandler: TemplateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
  protected val logTemplateExceptions: Boolean = false

  protected implicit val freemarkerConfig: Configuration = new Configuration(Configuration.VERSION_2_3_28)

  onConfigure {
    configureTemplateEngine()
  }

  protected def configureTemplateEngine(): Unit = {
    val configuration = new Configuration(Configuration.VERSION_2_3_28)

    if (templateFolder != null) {
      configuration.setDirectoryForTemplateLoading(templateFolder)
    } else if (classpathFolder != null) {
      configuration.setTemplateLoader(new ClassTemplateLoader(getClass, classpathFolder))
    }

    configuration.setDefaultEncoding(encoding)
    configuration.setTemplateExceptionHandler(templateExceptionHandler)
    configuration.setLogTemplateExceptions(logTemplateExceptions)
  }
}
