package acsgh.mad.scala.server

import acsgh.mad.scala.core.http.model.RequestMethod
import acsgh.mad.scala.server.router.http.directives.HttpDirectives
import acsgh.mad.scala.server.router.http.model.{HttpFilterAction, HttpRouteAction}
import acsgh.mad.scala.server.router.ws.directives.WSDirectives
import acsgh.mad.scala.server.router.ws.model.WSRouteAction

trait Controller extends HttpDirectives with WSDirectives {

  protected val builder: ServerBuilder

  def options(uri: String)(action: HttpRouteAction): Unit = builder.http.options(uri)(action)

  def get(uri: String)(action: HttpRouteAction): Unit = builder.http.get(uri)(action)

  def head(uri: String)(action: HttpRouteAction): Unit = builder.http.head(uri)(action)

  def post(uri: String)(action: HttpRouteAction): Unit = builder.http.post(uri)(action)

  def put(uri: String)(action: HttpRouteAction): Unit = builder.http.put(uri)(action)

  def patch(uri: String)(action: HttpRouteAction): Unit = builder.http.patch(uri)(action)

  def delete(uri: String)(action: HttpRouteAction): Unit = builder.http.delete(uri)(action)

  def trace(uri: String)(action: HttpRouteAction): Unit = builder.http.trace(uri)(action)

  def filter(uri: String, methods: Set[RequestMethod] = Set())(action: HttpFilterAction): Unit = builder.http.filter(uri, methods)(action)

  def resourceFolder(uri: String, resourceFolderPath: String): Unit = builder.http.resourceFolder(uri, resourceFolderPath)

  def filesystemFolder(uri: String, resourceFolderPath: String): Unit = builder.http.filesystemFolder(uri, resourceFolderPath)

  def webjars(): Unit = builder.http.webjars()

  def ws(uri: String, subprotocols: Set[String] = Set())(action: WSRouteAction): Unit = builder.ws.ws(uri, subprotocols)(action)
}
