ThisBuild / organization := "com.github.acsgh.mad.scala"
ThisBuild / scalaVersion := "2.13.1"

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val commonSettings = Seq(
  scalacOptions += "-feature",
  scalacOptions += "-deprecation",
  sonatypeProfileName := "com.github.acsgh",
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  ),
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % "1.5.13",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.slf4j" % "slf4j-api" % "1.7.21",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "org.scalacheck" %% "scalacheck" % "1.14.2" % Test,
    "org.pegdown" % "pegdown" % "1.4.2" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  ),
  homepage := Some(url("https://github.com/acsgh/mad-scala")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo in ThisBuild := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/acsgh/mad-scala"),
      "scm:git:git@github.com:acsgh/mad-scala.git"
    )
  ),
  developers := List(
    Developer("acsgh", "Alberto Crespo", "albertocresposanchez@gmail.com", url("https://github.com/acsgh"))
  )
)


lazy val root = (project in file("."))
  .settings(
    name := "mad-scala",
    commonSettings
  )
  .aggregate(
    core,
    routerHttp,
    routerWebsocket,
    converterJsonJackson,
    converterJsonSpray,
    converterTemplateFreemarker,
    converterTemplateThymeleaf,
    converterTemplateTwirl,
    providerServlet,
    providerJetty,
    providerNetty,
    supportSwagger,
    examplesJetty,
    examplesNetty
  )

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.github.acsgh.common.scala" %% "core" % "1.0.6"
    )
  )

lazy val routerHttp = (project in file("router/http"))
  .settings(
    organization := "com.github.acsgh.mad.scala.router",
    name := "http",
    commonSettings
  )
  .dependsOn(core)

lazy val routerWebsocket = (project in file("router/websocket"))
  .settings(
    organization := "com.github.acsgh.mad.scala.router",
    name := "websocket",
    commonSettings
  )
  .dependsOn(routerHttp)

lazy val converterJsonJackson = (project in file("converter/json/jackson"))
  .settings(
    organization := "com.github.acsgh.mad.scala.converter.json",
    name := "jackson",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"
    )
  )
  .dependsOn(routerHttp)

lazy val converterJsonSpray = (project in file("converter/json/spray"))
  .settings(
    organization := "com.github.acsgh.mad.scala.converter.json",
    name := "spray",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-json" % "1.3.5"
    )
  )
  .dependsOn(routerHttp)

lazy val converterTemplateFreemarker = (project in file("converter/template/freemarker"))
  .settings(
    organization := "com.github.acsgh.mad.scala.converter.template",
    name := "freemarker",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.freemarker" % "freemarker" % "2.3.28",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(routerHttp)

lazy val converterTemplateThymeleaf = (project in file("converter/template/thymeleaf"))
  .settings(
    organization := "com.github.acsgh.mad.scala.converter.template",
    name := "thymeleaf",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.thymeleaf" % "thymeleaf" % "3.0.11.RELEASE",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(routerHttp)

lazy val converterTemplateTwirl = (project in file("converter/template/twirl"))
  .settings(
    organization := "com.github.acsgh.mad.scala.converter.template",
    name := "twirl",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "twirl-api" % "1.4.2",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(routerHttp)

lazy val providerServlet = (project in file("provider/servlet"))
  .settings(
    organization := "com.github.acsgh.mad.scala.provider",
    name := "servlet",
    commonSettings,
    libraryDependencies ++= Seq(
      "javax.servlet" % "javax.servlet-api" % "4.0.1",
    )
  )
  .dependsOn(routerHttp)

lazy val providerJetty = (project in file("provider/jetty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.provider",
    name := "jetty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.eclipse.jetty" % "jetty-server" % "9.4.19.v20190610",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610",
      "org.eclipse.jetty.websocket" % "websocket-server" % "9.4.19.v20190610",
      "org.eclipse.jetty.websocket" % "websocket-servlet" % "9.3.6.v20151106",
    )
  )
  .dependsOn(routerWebsocket)
  .dependsOn(providerServlet)

lazy val providerNetty = (project in file("provider/netty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.provider",
    name := "netty",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.netty" % "netty-all" % "4.1.37.Final",
    )
  )
  .dependsOn(routerWebsocket)
  .dependsOn(providerServlet)

lazy val supportSwagger = (project in file("support/swagger"))
  .settings(
    organization := "com.github.acsgh.mad.scala.support",
    name := "swagger",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.swagger.core.v3" % "swagger-core" % "2.0.8",
      "io.swagger.core.v3" % "swagger-annotations" % "2.0.8",
      "io.swagger.core.v3" % "swagger-integration" % "2.0.8",
      "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.0.4",
      "org.webjars" % "swagger-ui" % "3.23.0"
    )
  )
  .dependsOn(routerHttp)

lazy val examplesJetty = (project in file("examples/jetty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.examples",
    name := "jetty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
    )
  )
  .dependsOn(converterTemplateThymeleaf)
  .dependsOn(converterJsonJackson)
  .dependsOn(providerJetty)
  .dependsOn(supportSwagger)

lazy val examplesNetty = (project in file("examples/netty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.examples",
    name := "netty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
    )
  )
  .dependsOn(converterTemplateThymeleaf)
  .dependsOn(converterJsonSpray)
  .dependsOn(providerNetty)
  .dependsOn(supportSwagger)
