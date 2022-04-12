import sbt.*
import Libs.*
import org.scalajs.linker.interface.ESVersion

import MyCompileOptions.optV3
import sbt.Keys.libraryDependencies
ThisBuild / scalaVersion := "3.1.1"
lazy val supportedScalaVersions = List("3.1.1")
val javart                      = "1.11"

scalaJSUseMainModuleInitializer := true
//scalaJSLinkerConfig ~= {
//  _.withModuleKind(ModuleKind.ESModule)
//}
//
//Test / scalaJSLinkerConfig ~= {
//  _.withModuleKind(ModuleKind.ESModule)
//}

addCommandAlias("rerun", "feRun ;backend/reStart")

inThisBuild {
  resolvers += Resolver.mavenLocal

  publishMavenStyle           := true
  bspEnabled                  := false
  organization                := "com.odenzo"
  reStart / mainClass         := Some("com.odenzo.webapp.be.BEMain")
  reStart / javaOptions += "-Xmx2g"
  reStartArgs                 := Seq("-x")
  Test / fork                 := false // ScalaJS can't be forked
  Test / parallelExecution    := false
  Test / logBuffered          := true
  scalacOptions ++= Seq("-release", "11")
  Compile / parallelExecution := false
}
ThisBuild / scalacOptions :=
  (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => optV3 // ++ lintersV3
    case _            => optV3 // ++ lintersV3
  })

lazy val root = project
  .in(file("."))
  .aggregate(xml.jvm, xml.js)
  .settings(name := "xplatform-xml-project", crossScalaVersions := supportedScalaVersions, doc / aggregate := false)

lazy val xml = (crossProject(JSPlatform, JVMPlatform))
  .crossType(CrossType.Full)
  .in(file("modules/xml-lib"))
  .settings(
    name := "xplatform-xml",
    libraryDependencies ++=
      Seq(
        XLib.cats.value,
        XLib.cats.value,
        XLib.catsEffect.value,
        XLib.pprint.value,
        XLib.scribe.value,
        XLib.scalaXML.value,
        XLib.munit.value,
        XLib.munitCats.value,
        XLib.fs2Data.value
        //  "eu.cdevreeze.yaidom" %%% "yaidom" % "1.13.0"

      )
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
addCommandAlias("to", "testOnly -- --tests=*JSDom")
