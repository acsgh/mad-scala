package com.acsgh.scala.mad.support.swagger.builder

import java.util

import com.acsgh.scala.mad.router.http.model.RequestMethod
import io.swagger.v3.oas.models._
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.{SecurityRequirement, SecurityScheme}
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag

import scala.collection.JavaConverters._

case class OpenApiBuilder(protected val delegate: OpenAPI = new OpenAPI()) extends Builder[OpenApiBuilder, OpenAPI] {

  override val root: OpenApiBuilder = this

  override val parent: OpenApiBuilder = this

  def info: InfoBuilder = {
    if (delegate.getInfo == null) {
      delegate.setInfo(new Info())
    }
    InfoBuilder(this, delegate.getInfo)
  }


  def schemaRequirement(name: String, securityScheme: SecurityScheme): OpenApiBuilder = {
    delegate.schemaRequirement(name, securityScheme)
    this
  }

  def path(name: String, path: PathItem): OpenAPI = delegate.path(name, path)

  def setComponents(components: Components): Unit = delegate.setComponents(components)

  def components: ComponentsBuilder = {
    if (delegate.getComponents == null) {
      delegate.setComponents(new Components())
    }
    ComponentsBuilder(this, delegate.getComponents)
  }

  def tag(name: String): TagBuilder[OpenApiBuilder] = {
    if (delegate.getTags == null) {
      delegate.setTags(new util.ArrayList[Tag]())
    }

    tags
      .find(_.getName == name)
      .map(TagBuilder[OpenApiBuilder](this, _))
      .getOrElse {
        val tag = new Tag()
        delegate.addTagsItem(tag)
        TagBuilder[OpenApiBuilder](this, tag).name(name)
      }
  }

  def tags(tags: List[Tag]): OpenApiBuilder = {
    delegate.setTags(tags.asJava)
    this
  }

  def tags: List[Tag] = List() ++ delegate.getTags.asScala

  def security(name: String, items: String*): OpenApiBuilder = {
    if (security.isEmpty) {
      delegate.addSecurityItem(new SecurityRequirement)
    }

    security.head.addList(name, items.toList.asJava)

    this
  }

  def security: List[SecurityRequirement] = List() ++ delegate.getSecurity.asScala

  def server(url: String): ServerBuilder = {
    if (delegate.getServers == null) {
      delegate.setServers(new util.ArrayList[Server]())
    }

    servers
      .find(_.getUrl == url)
      .map(ServerBuilder(this, _))
      .getOrElse {
        val serverDelegate = new Server()
        delegate.addServersItem(serverDelegate)
        ServerBuilder(this, serverDelegate).url(url)
      }
  }

  def servers(servers: List[Server]): OpenApiBuilder = {
    delegate.setServers(servers.asJava)
    this
  }

  def servers: List[Server] = List() ++ delegate.getServers.asScala

  def externalDocs: ExternalDocumentationsBuilder[OpenApiBuilder] = {
    if (delegate.getExternalDocs == null) {
      delegate.setExternalDocs(new ExternalDocumentation())
    }
    ExternalDocumentationsBuilder(this, delegate.getExternalDocs)
  }

  def extensions: Map[String, AnyRef] = Map() ++ delegate.getExtensions.asScala

  def extensions(extensions: Map[String, AnyRef]): OpenApiBuilder = {
    delegate.setExtensions(extensions.asJava)
    this
  }

  def extension(key: String): Option[AnyRef] = delegate.getExtensions.asScala.get(key)

  def extension(key: String, value: AnyRef): OpenApiBuilder = {
    delegate.addExtension(key, value)
    this
  }

  def operation(uri: String, method: RequestMethod, operation: Operation): OpenApiBuilder = {
    this
  }
}
