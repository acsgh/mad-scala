ThisBuild / organization := "com.github.acsgh.mad.scala"
ThisBuild / scalaVersion := "2.13.2"

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
  crossScalaVersions := List("2.12.11", "2.13.2"),
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % "1.6.1",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.scalatest" %% "scalatest" % "3.2.0" % Test,
    "org.scalacheck" %% "scalacheck" % "1.14.3" % Test,
    "org.pegdown" % "pegdown" % "1.6.0" % Test,
    "org.scalamock" %% "scalamock" % "4.4.0" % Test,
  ),
  homepage := Some(url("https://github.com/acsgh/mad-scala")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  publishTo := sonatypePublishToBundle.value,
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
    commonSettings,
    crossScalaVersions := Nil,
    publish / skip := true
  )
  .aggregate(
    core,
    serverCore,
    serverProviderNetty,
    serverProviderServlet,
    serverProviderJetty,
    serverConverterJsonJackson,
    serverConverterJsonSpray,
    serverConverterTemplateFreemarker,
    serverConverterTemplateThymeleaf,
    serverConverterTemplateTwirl,
    serverSupportSwagger,
    serverExampleNetty,
    serverExampleJetty
  )

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.github.acsgh.common.scala" %% "core" % "1.2.16",
    )
  )

lazy val serverCore = (project in file("server/core"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server",
    name := "core",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.cache2k" % "cache2k-core" % "1.2.4.Final"
    )
  )
  .dependsOn(core)

lazy val serverProviderNetty = (project in file("server/provider/netty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.provider",
    name := "netty",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.netty" % "netty-all" % "4.1.50.Final"
    )
  )
  .dependsOn(serverCore)

lazy val serverProviderServlet = (project in file("server/provider/servlet"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.provider",
    name := "servlet",
    commonSettings,
    libraryDependencies ++= Seq(
      "javax.servlet" % "javax.servlet-api" % "4.0.1"
    )
  )
  .dependsOn(serverCore)

val jettyVersion = "9.4.30.v20200611"

lazy val serverProviderJetty = (project in file("server/provider/jetty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.provider",
    name := "jetty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.eclipse.jetty" % "jetty-server" % jettyVersion,
      "org.eclipse.jetty" % "jetty-webapp" % jettyVersion,
      "org.eclipse.jetty.websocket" % "websocket-server" % jettyVersion,
      "org.eclipse.jetty.websocket" % "websocket-servlet" % jettyVersion
    )
  )
  .dependsOn(serverProviderServlet)

lazy val serverConverterJsonJackson = (project in file("server/converter/json/jackson"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.converter.json",
    name := "jackson",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"
    )
  )
  .dependsOn(serverCore)

lazy val serverConverterJsonSpray = (project in file("server/converter/json/spray"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.converter.json",
    name := "spray",
    commonSettings,
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-json" % "1.3.5"
    )
  )
  .dependsOn(serverCore)

lazy val serverConverterTemplateFreemarker = (project in file("server/converter/template/freemarker"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.converter.template",
    name := "freemarker",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.freemarker" % "freemarker" % "2.3.28",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(serverCore)

lazy val serverConverterTemplateThymeleaf = (project in file("server/converter/template/thymeleaf"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.converter.template",
    name := "thymeleaf",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.thymeleaf" % "thymeleaf" % "3.0.11.RELEASE",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(serverCore)

lazy val serverConverterTemplateTwirl = (project in file("server/converter/template/twirl"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.converter.template",
    name := "twirl",
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "twirl-api" % "1.4.2",
      "com.googlecode.htmlcompressor" % "htmlcompressor" % "1.5.2"
    )
  )
  .dependsOn(serverCore)

lazy val serverSupportSwagger = (project in file("server/support/swagger"))
  .settings(
    organization := "com.github.acsgh.mad.scala.server.support",
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
  .dependsOn(serverCore)

lazy val serverExampleNetty = (project in file("examples/server/netty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.examples.server",
    name := "netty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
    )
  )
  .dependsOn(serverProviderNetty)
  .dependsOn(serverConverterTemplateThymeleaf)
  .dependsOn(serverConverterJsonSpray)
  .dependsOn(serverSupportSwagger)

lazy val serverExampleJetty = (project in file("examples/server/jetty"))
  .settings(
    organization := "com.github.acsgh.mad.scala.examples.server",
    name := "jetty",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
    )
  )
  .dependsOn(serverProviderJetty)
  .dependsOn(serverConverterTemplateThymeleaf)
  .dependsOn(serverConverterJsonSpray)
  .dependsOn(serverSupportSwagger)
