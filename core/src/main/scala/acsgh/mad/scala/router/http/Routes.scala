package acsgh.mad.scala.router.http

import java.io.File

import acsgh.mad.scala.router.http.convertions.{DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.files.{StaticClasspathFolderFilter, StaticFilesystemFolderFilter}
import acsgh.mad.scala.router.http.model.RequestMethod._
import acsgh.mad.scala.router.http.model.{FilterAction, HttpRoute, RequestMethod, RouteAction}

trait Routes extends DefaultFormats with DefaultParamHandling with Directives {

  protected val httpRouter: HttpRouter

  def options(uri: String)(action: RouteAction): Unit = servlet(uri, OPTIONS)(action)

  def get(uri: String)(action: RouteAction): Unit = servlet(uri, GET)(action)

  def head(uri: String)(action: RouteAction): Unit = servlet(uri, HEAD)(action)

  def post(uri: String)(action: RouteAction): Unit = servlet(uri, POST)(action)

  def put(uri: String)(action: RouteAction): Unit = servlet(uri, PUT)(action)

  def patch(uri: String)(action: RouteAction): Unit = servlet(uri, PATCH)(action)

  def delete(uri: String)(action: RouteAction): Unit = servlet(uri, DELETE)(action)

  def trace(uri: String)(action: RouteAction): Unit = servlet(uri, TRACE)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: FilterAction): Unit = {
    httpRouter.filter(new HttpRoute[FilterAction](uri, methods, action))
  }

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = 8//servlet(uri, Set(RequestMethod.GET))(StaticClasspathFolderFilter(resourceFolderPath).handle)

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = 8//servlet(uri, Set(RequestMethod.GET))(StaticFilesystemFolderFilter(new File(resourceFolderPath)).handle)

  def webjars(): Unit = resourceFolder("/webjars/{path+}", "META-INF/resources/webjars")

  protected def servlet(uri: String, method: RequestMethod)(action: RouteAction): Unit = {
    httpRouter.servlet(new HttpRoute[RouteAction](uri, Set(method), action))
  }
}
