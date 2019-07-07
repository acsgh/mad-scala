package com.acs.scala.server.mad.converter.template.freemarker

import java.io.File

import com.acs.scala.server.mad.router.HttpServer
import freemarker.cache.ClassTemplateLoader
import freemarker.template.{Configuration, TemplateExceptionHandler}

trait FreemarkerHttpServer extends  FreemarkerDirectives {
  server: HttpServer =>

  protected val templateFolder: File
  protected val classpathFolder: String
  protected val encoding: String = "UTF-8"
  protected val templateExceptionHandler: TemplateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
  protected val logTemplateExceptions: Boolean = false

  private[freemarker] val freemarkerConfig: Configuration = new Configuration(Configuration.VERSION_2_3_28)

  onConfigure {
    configureTemplateEngine()
  }


  override protected val freemarkerHttpServer: FreemarkerHttpServer = this

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
