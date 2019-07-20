package com.acsgh.scala.mad.support.swagger.dsl

import com.acsgh.scala.mad.router.http.model.RequestMethod

case class OpenAPI
(
  openapi: String = "3.0.1",
  info: Info = null,
  externalDocs: ExternalDocumentation = null,
  servers: List[Server] = null,
  extensions: Map[String, AnyRef] = null
) {

  def operation(uri: String, method: RequestMethod, operation: Operation): OpenAPI = {
    this
  }
}

//private List<Server> servers = null;
//private List<SecurityRequirement> security = null;
//private List<Tag> tags = null;
//private Paths paths = null;
//private Components components = null;

case class Info
(
  title: String = null,
  description: String = null,
  termsOfService: String = null,
  contact: Contact = null,
  license: License = null,
  version: String = null,
  extensions: Map[String, AnyRef] = null
)

case class Contact
(
  name: String = null,
  url: String = null,
  email: String = null,
  extensions: Map[String, AnyRef] = null
)

case class License
(
  name: String = null,
  url: String = null,
  extensions: Map[String, AnyRef] = null
)

case class ExternalDocumentation
(
  description: String = null,
  url: String = null,
  extensions: Map[String, AnyRef] = null
)

case class Server
(
  url: String = null,
  description: String = null,
  variables: ServerVariables = null,
  extensions: Map[String, AnyRef] = null
)

case class ServerVariables
(

  values: Map[String, ServerVariable] = null,
  extensions: Map[String, AnyRef] = null
)

case class ServerVariable
(
  enum: Set[String] = null,
  default: String = null,
  description: String = null,
  extensions: Map[String, AnyRef] = null
)

case class Operation
(

)
