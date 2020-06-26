package acsgh.mad.scala.examples.netty

import acsgh.mad.scala.server.converter.template.thymeleaf.{ThymeleafDirectives, ThymeleafTemplate}
import acsgh.mad.scala.server.{Controller, ServerBuilder}
import org.thymeleaf.TemplateEngine

case class FormsRoutes(builder: ServerBuilder)(implicit protected val thymeleafEngine: TemplateEngine) extends Controller with JsonProtocol with ThymeleafDirectives {


  get("/forms") { implicit context =>
    val fname = "Jonh"
    val lname = "Doe"
    val fileSize = "1 kb"
    responseBody(ThymeleafTemplate("forms", Map()))
  }

  post("/forms/regular") { implicit context =>
    requestFormParam("fname", "lname") { (fname, lname) =>
      responseBody(ThymeleafTemplate("forms", Map(
        "fname" -> fname,
        "lname" -> lname,
      )))
    }
  }

  post("/forms/multipart") { implicit context =>
    requestMultipartParam("fname", "lname") { (fname, lname) =>
      responseBody(ThymeleafTemplate("forms", Map(
        "fname" -> fname,
        "lname" -> lname,
      )))
    }
  }
}
