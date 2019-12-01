package acsgh.mad.scala.server.router.http

import java.io.File

import acsgh.mad.scala.core.http.model.RequestMethod
import acsgh.mad.scala.server.router.http.convertions.{DefaultFormats, DefaultParamHandling}
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.files.{StaticClasspathFolderFilter, StaticFilesystemFolderFilter}
import acsgh.mad.scala.core.http.model.RequestMethod._
import acsgh.mad.scala.server.router.http.model.{FilterAction, HttpRoute, Route, RouteAction}

trait HttpRoutes extends DefaultFormats with DefaultParamHandling with HttpDirectives {

  def options(uri: String)(action: RouteAction): Unit = servlet(uri, OPTIONS)(action)

  def get(uri: String)(action: RouteAction): Unit = servlet(uri, GET)(action)

  def head(uri: String)(action: RouteAction): Unit = servlet(uri, HEAD)(action)

  def post(uri: String)(action: RouteAction): Unit = servlet(uri, POST)(action)

  def put(uri: String)(action: RouteAction): Unit = servlet(uri, PUT)(action)

  def patch(uri: String)(action: RouteAction): Unit = servlet(uri, PATCH)(action)

  def delete(uri: String)(action: RouteAction): Unit = servlet(uri, DELETE)(action)

  def trace(uri: String)(action: RouteAction): Unit = servlet(uri, TRACE)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: FilterAction): Unit = filter(HttpRoute[FilterAction](uri, methods, action))

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = servlet(StaticClasspathFolderFilter(assetsUri(uri), resourceFolderPath))

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = servlet(StaticFilesystemFolderFilter(assetsUri(uri), new File(resourceFolderPath)))

  def webjars(): Unit = resourceFolder("/webjars", "META-INF/resources/webjars")

  protected def servlet(route: Route[RouteAction]): Unit

  protected def filter(route: Route[FilterAction]): Unit

  def servlet(uri: String, method: RequestMethod)(action: RouteAction): Unit = servlet(HttpRoute[RouteAction](uri, Set(method), action))

  protected def assetsUri(uri: String): String = {
    if (uri.contains("*")) {
      throw new IllegalArgumentException("Assets folder cannot contains *")
    }
    s"$uri/{path+}".replace("//", "/")
  }
}
