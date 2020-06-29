package acsgh.mad.scala.examples.jetty

import java.io.{BufferedOutputStream, FileOutputStream}

import acsgh.mad.scala.server.converter.template.thymeleaf.{ThymeleafDirectives, ThymeleafTemplate}
import acsgh.mad.scala.server.router.http.params.reader.default._
import acsgh.mad.scala.server.{Controller, ServerBuilder}
import org.thymeleaf.TemplateEngine

case class FormsRoutes(builder: ServerBuilder)(implicit protected val thymeleafEngine: TemplateEngine) extends Controller with JsonProtocol with ThymeleafDirectives {


  get("/forms") { implicit context =>
    val fname = "Jonh"
    val lname = "Doe"
    val fileSize = "1 kb"
    responseBody(ThymeleafTemplate("forms", Map()))
  }

  post("/forms") { implicit context =>
    formParam("fname", "lname", "fileupload".multipartFile.opt) { (fname, lname, fileupload) =>

      val params = fileupload.fold {
        Map(
          "fname" -> fname,
          "lname" -> lname
        )
      } { file =>
        val bos = new BufferedOutputStream(new FileOutputStream(file.filename.getOrElse("file.txt")))
        bos.write(file.content)
        bos.close()

        Map(
          "fname" -> fname,
          "lname" -> lname,
          "fileName" -> file.filename.getOrElse("unknown"),
          "fileSize" -> s"${file.content.length / 1024} Kb",
        )
      }

      responseBody(ThymeleafTemplate("forms", params))
    }
  }
}
