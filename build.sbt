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
publishTo <<= version {MavenCentralSettings.publishURL(_)}
credentials ++= CredentialsSettings.publishCredentials

//---------------------------------------
// Compiler directives
//---------------------------------------

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-Xlint")
scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-target:jvm-1.7")

scalacOptions in (Compile, doc) ++= Seq("-doc-title", "SBT Scaladoc Settings Plugin")
scalacOptions in (Compile, doc) ++= org.dmonix.sbt.ScalaDocSettings.rootDoc
scalacOptions in (Compile, doc) ++= Seq("-doc-footer", "Copyright (c) 2017 Peter Nerg, Apache License v2.0.")

//---------------------------------------
// Scaladoc generation
// Uses the built in plugin by this project
// Executes during doc generations.
// Recursively copies all the doc-files to the output target
//---------------------------------------
copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)

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
