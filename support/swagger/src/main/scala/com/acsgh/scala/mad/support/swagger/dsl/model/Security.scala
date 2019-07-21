package com.acsgh.scala.mad.support.swagger.dsl.model

import enumeratum._

object Security {

  sealed trait Type extends EnumEntry

  object Type extends Enum[Type] {
    val values = findValues

    case object APIKEY extends Type

    case object HTTP extends Type

    case object OAUTH2 extends Type

    case object OPENIDCONNECT extends Type

  }

  sealed trait In extends EnumEntry

  object In extends Enum[In] {
    val values = findValues

    case object COOKIE extends In

    case object HEADER extends In

    case object QUERY extends In

  }

}

case class OAuthFlows
(
  `implicit`: OAuthFlow = null,
  password: OAuthFlow = null,
  clientCredentials: OAuthFlow = null,
  authorizationCode: OAuthFlow = null,
  extensions: Map[String, AnyRef] = null
)

case class OAuthFlow
(
  authorizationUrl: String = null,
  tokenUrl: String = null,
  refreshUrl: String = null,
  scopes: Scopes = null,
  extensions: Map[String, AnyRef] = null
)

case class Scopes
(
  values: Map[String, String] = null,
  extensions: Map[String, AnyRef] = null
)


case class SecurityScheme
(
  `type`: Security.Type = null,
  description: String = null,
  name: String = null,
  ref: String = null,
  in: Security.In = null,
  scheme: String = null,
  bearerFormat: String = null,
  flows: OAuthFlows = null,
  openIdConnectUrl: String = null,
  extensions: Map[String, AnyRef] = null
)