/**
  *  Copyright 2017 Peter Nerg
  *
  *  Licensed under the Apache License, Version 2.0 (the "License");
  *  you may not use this file except in compliance with the License.
  *  You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  *  Unless required by applicable law or agreed to in writing, software
  *  distributed under the License is distributed on an "AS IS" BASIS,
  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *  See the License for the specific language governing permissions and
  *  limitations under the License.
  */
package org.dmonix.sbt
import java.io.File

import sbt.Keys.{commands, name, version}
import sbt.Opts._
import sbt._
import sbt.plugins._

import scala.annotation.target


/**
  * Settings to use when configuring the SBT build file building Scala Doc.

  * @author Peter Nerg
  */
object ScalaDocSettings extends AutoPlugin {
  override def requires = JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    lazy val copyDocAssetsTask = taskKey[Unit]("Copies all the doc-files directories to the target/API output directory")
  }

  // This is to make all the above autoImport values into scope
  import org.dmonix.sbt.ScalaDocSettings.autoImport._

  // This adds the ''packageModule'' command to the set of available commands in SBT
  override lazy val projectSettings = scaladocPluginTasks

  //  //ugly hack to copy the scaladoc doc-files
//  lazy val copyDocAssetsTask = taskKey[Unit]("Copy doc assets")
//  copyDocAssetsTask := {
//    println(baseDirectory.value)
//    println(file("src/main/scaladoc/root-doc.txt").getAbsolutePath)
//    val sourceDir = file("src/main/scaladoc/doc-files")
//    //  val targetDir = (target in (Compile, doc)).value
//    val targetDir = file("target/api/doc-files")
//    println(s"Copying doc assets[$sourceDir]->[$targetDir]")
//    IO.copyDirectory(sourceDir, targetDir)
//  }
//  copyDocAssetsTask <<= copyDocAssetsTask triggeredBy (doc in Compile)

  lazy val scaladocPluginTasks: Seq[Def.Setting[_]] = Seq(
    copyDocAssetsTask <<= (Keys.target in Docs, Keys.baseDirectory in Docs) map { (t: java.io.File, b: java.io.File) => {
      println("=======task1======")
      println("task1-base:"+b)
      println("task1-target:"+t)
//      println(file("task1-:src/main/scaladoc/root-doc.txt").getAbsolutePath)
//      val sourceDir = file("src/main/scaladoc/doc-files")
//      //  val targetDir = (target in (Compile, doc)).value
//      val targetDir = file("target/api/doc-files")
//      println(s"Copying doc assets[$sourceDir]->[$targetDir]")
//      IO.copyDirectory(sourceDir, targetDir)
      println("=======task1======")
    }
    })

  /** Creates the settings needed to add a doc root to the scaladoc build.
    *
    * By default this method expects there to be a file ''src/main/scaladoc/overview.txt''.
    *
    * Example of usage in the build.sbt:
    *
    *
    * {{{
    * import org.dmonix.sbt.ScalaDocSettings._
    * scalacOptions in (Compile, doc) ++= rootDoc
    * }}}
    * @return A sequence with the necessary configuration
    * @since 0.5
    */
  def rootDoc:Seq[String] = rootDoc(new File("src/main/scaladoc/overview.txt").getAbsolutePath)

  /** Creates the settings needed to add a doc root to the scaladoc build.
    **
    * Example of usage in the build.sbt:
    *
    * {{{
    * import org.dmonix.sbt.ScalaDocSettings._
    * scalacOptions in (Compile, doc) ++= rootDoc("path")
    * }}}
    * @param rootDocFile The path to the root/overview file
    * @return A sequence with the necessary configuration
    * @since 0.5
    */
  def rootDoc(rootDocFile:String):Seq[String] = Seq("-doc-root-content", rootDocFile)

}
