package acsgh.mad.scala.router.http

import java.io.File

import acsgh.mad.scala.router.http.convertions.{DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.files.{StaticClasspathFolderFilter, StaticFilesystemFolderFilter}
import acsgh.mad.scala.router.http.model.RequestMethod._
import acsgh.mad.scala.router.http.model.{RequestMethod, Response}

trait Routes extends DefaultFormats with DefaultParamHandling with Directives {

  def options(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, OPTIONS)(action)

  def get(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, GET)(action)

  def head(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, HEAD)(action)

  def post(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, POST)(action)

  def put(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, PUT)(action)

  def patch(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, PATCH)(action)

  def delete(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, DELETE)(action)

  def trace(uri: String)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = servlet(uri, TRACE)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: RequestContext => (() => Response) => Response)(implicit httpRouter: HttpRouter): Unit = {
    httpRouter.filter(new HttpRoute[RequestFilter](uri, methods, new RequestFilter {
      override def handle(nextJump: () => Response)(implicit context: RequestContext): Response = action(context)(nextJump)
    }))
  }

  def resourceFolder(uri: String, resourceFolderPath: String)(implicit httpRouter: HttpRouter): Unit = filterInt(uri, Set(RequestMethod.GET))(StaticClasspathFolderFilter(resourceFolderPath))

  def filesystemFolder(uri: String, resourceFolderPath: String)(implicit httpRouter: HttpRouter): Unit = filterInt(uri, Set(RequestMethod.GET))(StaticFilesystemFolderFilter(new File(resourceFolderPath)))

  def webjars()(implicit httpRouter: HttpRouter): Unit = resourceFolder("/webjars/{path+}", "META-INF/resources/webjars")

  protected def servlet(uri: String, method: RequestMethod)(action: RequestContext => Response)(implicit httpRouter: HttpRouter): Unit = {
    httpRouter.servlet(new HttpRoute[RequestServlet](uri, Set(method), (context: RequestContext) => action(context)))
  }

  protected def filterInt(uri: String, methods: Set[RequestMethod] = Set())(action: RequestFilter)(implicit httpRouter: HttpRouter): Unit = {
    httpRouter.filter(new HttpRoute[RequestFilter](uri, methods, action))
  }

}
