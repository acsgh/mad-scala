package com.acs.scala.server.mad.router

import java.io.File

import com.acs.scala.server.mad.router.convertions.{DefaultFormats, DefaultParamHandling}
import com.acs.scala.server.mad.router.directives.Directives
import com.acs.scala.server.mad.router.files.{StaticClasspathFolderFilter, StaticFilesystemFolderFilter}
import com.acs.scala.server.mad.router.model.{RequestMethod, Response}

trait Routes extends DefaultFormats with DefaultParamHandling with Directives {

  protected val httpRouter: HttpRouter

  def options(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.OPTIONS)(action)

  def get(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.GET)(action)

  def head(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.HEAD)(action)

  def post(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.POST)(action)

  def put(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.PUT)(action)

  def patch(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.PATCH)(action)

  def delete(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.DELETE)(action)

  def trace(uri: String)(action: RequestContext => Response): Unit = servlet(uri, RequestMethod.TRACE)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: RequestContext => (() => Response) => Response): Unit = {
    httpRouter.filter(new HttpRoute[RequestFilter](uri, methods, new RequestFilter {
      override def handle(nextJump: () => Response)(implicit context: RequestContext): Response = action(context)(nextJump)
    }))
  }

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = filterInt(uri, Set(RequestMethod.GET))(StaticClasspathFolderFilter(resourceFolderPath))

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = filterInt(uri, Set(RequestMethod.GET))(StaticFilesystemFolderFilter(new File(resourceFolderPath)))

  def webjars(): Unit = resourceFolder("/webjars/{path+}", "META-INF/resources/webjars")

  private def servlet(uri: String, method: RequestMethod)(action: RequestContext => Response): Unit = {
    httpRouter.servlet(new HttpRoute[RequestServlet](uri, Set(method), (context: RequestContext) => action(context)))
  }

  private def filterInt(uri: String, methods: Set[RequestMethod] = Set())(action: RequestFilter): Unit = {
    httpRouter.filter(new HttpRoute[RequestFilter](uri, methods, action))
  }

}
