package acsgh.mad.scala.converter.template.freemarker

import java.io.File

import freemarker.cache.ClassTemplateLoader
import freemarker.template.{Configuration, TemplateExceptionHandler}

object FreemarkerConfigurationProvider {

  def buildFromFilesystem(templateFolder: File, templateExceptionHandler: TemplateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER, logTemplateExceptions: Boolean = false, encoding: String = "UTF-8"): Configuration = {
    val configuration = new Configuration(Configuration.VERSION_2_3_28)

    configuration.setDirectoryForTemplateLoading(templateFolder)
    configuration.setDefaultEncoding(encoding)
    configuration.setTemplateExceptionHandler(templateExceptionHandler)
    configuration.setLogTemplateExceptions(logTemplateExceptions)

    configuration
  }

  def buildFromClasspath(classpathFolder: String, templateExceptionHandler: TemplateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER, logTemplateExceptions: Boolean = false, encoding: String = "UTF-8"): Configuration = {
    val configuration = new Configuration(Configuration.VERSION_2_3_28)

    configuration.setTemplateLoader(new ClassTemplateLoader(getClass, classpathFolder))
    configuration.setDefaultEncoding(encoding)
    configuration.setTemplateExceptionHandler(templateExceptionHandler)
    configuration.setLogTemplateExceptions(logTemplateExceptions)

    configuration
  }
}
