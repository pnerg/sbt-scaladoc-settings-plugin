lazy val `sbt-release` = project in file(".")

sbtPlugin := true

name := "sbt-scaladoc-settings-plugin"
organization := "org.dmonix.sbt"

//sbt 13.x is built using Scala 2.10 hence we can't use 2.11 to build plugins
scalaVersion := "2.10.6"

//----------------------------
//Info for where and how to publish artifacts
//Uses the plugin from : https://github.com/pnerg/sbt-scaladoc-settings-plugin
//----------------------------
import org.dmonix.sbt._
publishTo <<= version {MavenCentralSettings.deployURL(_)}
credentials ++= CredentialsSettings.publishCredentials

//---------------------------------------
// Compiler directives
//---------------------------------------

// allow circular dependencies for test sources
compileOrder in Test := CompileOrder.Mixed

javacOptions ++= Seq("-source", "1.7", "-target", "1.8", "-Xlint")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-target:jvm-1.7")

scalacOptions in (Compile, doc) ++= Seq("-doc-title", "SBT Scaladoc Settings Plugin")
scalacOptions in (Compile, doc) ++= org.dmonix.sbt.ScalaDocSettings.rootDoc
scalacOptions in (Compile, doc) ++= Seq("-doc-footer", "Copyright (c) 2017 Peter Nerg, Apache License v2.0.")

//ugly hack to copy the scaladoc doc-files
lazy val copyDocAssetsTask2 = taskKey[Unit]("Copy doc assets")
copyDocAssetsTask2 := {
  println("=======task2======")
  println("task2-base: "+baseDirectory.value)
  println("task2-target1: "+target.value)
  println("task2-target2: "+(target in (Compile, doc)).value)
  //println(file("src/main/scaladoc/root-doc.txt").getAbsolutePath)
  //val sourceDir = file("src/main/scaladoc/doc-files")
  //  val targetDir = (target in (Compile, doc)).value
  //val targetDir = file("target/api/doc-files")
  //println(s"Copying doc assets[$sourceDir]->[$targetDir]")
  //IO.copyDirectory(sourceDir, targetDir)
  println("=======task2======")
}

copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)
copyDocAssetsTask2 <<= copyDocAssetsTask2 triggeredBy (doc in Compile)

//---------------------------------------

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

//---------------------------------------

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := {
  if (scalaBinaryVersion.value == "2.10") false
  else false
}

//----------------------------
//needed to create the proper pom.xml for publishing to mvn central
//----------------------------
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra := (
  <url>https://github.com/pnerg/sbt-scaladoc-settings-plugin</url>
    <licenses>
      <license>
        <name>Apache</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:pnerg/sbt-scaladoc-settings-plugin.git</url>
      <connection>scm:git:git@github.com/pnerg/sbt-scaladoc-settings-plugin.git</connection>
    </scm>
    <developers>
      <developer>
        <id>pnerg</id>
        <name>Peter Nerg</name>
        <url>http://github.com/pnerg</url>
      </developer>
    </developers>)
