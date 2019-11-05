package acsgh.mad.scala

import acsgh.mad.scala.router.http.directives.Directives
import acsgh.mad.scala.router.http.model.{FilterAction, RequestMethod, RouteAction}
import acsgh.mad.scala.router.ws.directives.WSDirectives
import acsgh.mad.scala.router.ws.model.WSRouteAction

trait Controller extends Directives with WSDirectives {

  protected val builder: ServerBuilder

  def options(uri: String)(action: RouteAction): Unit = builder.http.options(uri)(action)

  def get(uri: String)(action: RouteAction): Unit = builder.http.get(uri)(action)

  def head(uri: String)(action: RouteAction): Unit = builder.http.head(uri)(action)

  def post(uri: String)(action: RouteAction): Unit = builder.http.post(uri)(action)

  def put(uri: String)(action: RouteAction): Unit = builder.http.put(uri)(action)

  def patch(uri: String)(action: RouteAction): Unit = builder.http.patch(uri)(action)

  def delete(uri: String)(action: RouteAction): Unit = builder.http.delete(uri)(action)

  def trace(uri: String)(action: RouteAction): Unit = builder.http.trace(uri)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: FilterAction): Unit = builder.http.filter(uri, methods)(action)

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = builder.http.resourceFolder(uri, resourceFolderPath)

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = builder.http.filesystemFolder(uri, resourceFolderPath)

  def webjars(): Unit = builder.http.webjars()

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRouteAction): Unit = builder.ws.ws(uri, subprotocols)(action)
}
