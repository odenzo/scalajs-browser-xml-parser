import sbt.*
import Libs.*
import org.scalajs.linker.interface.ESVersion
import MyCompileOptions.{optV2, optV3}
import sbt.Keys.libraryDependencies
ThisBuild / scalaVersion := "2.13.8"
lazy val supportedScalaVersions = List("2.13.8", "3.1.2")
val javart                      = "1.11"

inThisBuild {
  resolvers += Resolver.mavenLocal
  publishMavenStyle           := true
  bspEnabled                  := false
  organization                := "com.odenzo"
  reStart / javaOptions += "-Xmx2g"
  Test / fork                 := false // ScalaJS can't be forked
  Test / parallelExecution    := false
  Test / logBuffered          := true
  Compile / parallelExecution := false
  scalacOptions               :=
    (CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) => optV3
      case _            => optV2
    })
  scalacOptions ++= Seq("-release", "11")
}
lazy val root = project
  .in(file("."))
  .aggregate(xml.jvm, xml.js)
  .settings(name := "xplatform-xml-project", crossScalaVersions := supportedScalaVersions, doc / aggregate := false)

lazy val xml = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("modules/xml-lib"))
  .settings(
    name := "xplatform-xml",
    libraryDependencies ++=
      Seq(XLib.cats.value, XLib.pprint.value, XLib.scribe.value, XLib.scalaXML.value, XLib.munit.value)
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.1.0"
      // Has to be in plugins.sbt
      // "org.scala-js"   % "scalajs-env-jsdom-nodejs_213.8" % "1.1.0"
    ),
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
  )

ThisBuild / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
addCommandAlias("to", "xmlJS/testOnly -- --tests=")
