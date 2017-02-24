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

import sbt._
import sbt.plugins._

import scala.annotation.tailrec


/**
  * Settings to use when configuring the SBT build file building Scala Doc.

  * @author Peter Nerg
  */
object ScalaDocSettings extends AutoPlugin {
  override def requires = JvmPlugin
  override def trigger = allRequirements

  object autoImport {
    lazy val processPlantUML = SettingKey[Boolean]("If the plugin shall attempt to render images of all PlantUML diagrams it finds in the doc-files directories")
    lazy val plantUMLExtension = SettingKey[String]("The extension for all the PlantUML files  (default '.puml')")
    lazy val copyDocAssetsTask = taskKey[Unit]("Copies all the doc-files directories to the target/API output directory")
  }

  // This is to make all the above autoImport values into scope
  import org.dmonix.sbt.ScalaDocSettings.autoImport._

  override def globalSettings = Seq(
    processPlantUML := true,
    plantUMLExtension := ".puml"
  )

  // This adds the ''packageModule'' command to the set of available commands in SBT
  override lazy val projectSettings = scaladocPluginTasks

  lazy val scaladocPluginTasks: Seq[Def.Setting[_]] = Seq(
      copyDocAssetsTask <<= (Keys.target in (Compile, Keys.doc), Keys.sourceDirectory in Compile, processPlantUML, plantUMLExtension) map { (apiTargetDir: File, sourceDir: File, processPlantUML:Boolean, plantUMLExtension:String) => {
        def isPlantUMLFile(file:File):Boolean = {
          if (processPlantUML) {
            file.name.endsWith(plantUMLExtension)
          }
          else {
            false
          }
        }

        import PlantUMLGenerator._
        val scalaDocDir = new File(sourceDir, "/scaladoc/") //the root dir for the Scaladoc, normally src/main/scaladoc
        def mapToTargetBound = mapToTarget (scalaDocDir)(apiTargetDir) _
        listDocFileDirs(scalaDocDir).foreach(docFileDir => {
          //partition the files in the doc-files directory to be plantUML and non-plantUML files
          val files = docFileDir.listFiles().partition(isPlantUMLFile)

          //map the input doc-files dir to the target doc-files dir
          val outDir = mapToTargetBound(docFileDir)
          outDir.mkdirs()

          //all plantUML files that shall be rendered, may be empty in case no files or processPlantUML=false
          files._1.foreach(f => renderImage(f, outDir))

          //all files that just should be copied
          files._2.foreach(f => {
            println(s"Copying doc asset [$f]")
            IO.copyFile(f, new File(outDir, f.getName))
          })
        })
      }
    } triggeredBy(Keys.doc in Compile))

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

  /**
    * Performs a recursive iteration over the provided path listing all ''doc-files'' directories
    * @param root The root file to start from
    * @return A list of all found ''doc-files'' directories
    */
  def listDocFileDirs(root: File): List[File] = {
    @tailrec
    def recListDirs(files: List[File], accumulator: List[File]): List[File] = files match {
      //End of recursion
      case Nil =>
        accumulator
      case head :: tail =>
        if(head.isDirectory) //add the directory and continue the recursion
          recListDirs(Option(head.listFiles).map(_.toList ::: tail).getOrElse(tail), head::accumulator)
        else //files are not added with the list we have
          recListDirs(tail, accumulator)
    }
    recListDirs(List(root), Nil).filter(_.getName.equalsIgnoreCase("doc-files")) //add a filter on the found directories
  }

  /**
    * Maps the target ''doc-files'' to the path it shall be copied to under the ''target'' dir.
    *
    * E.g. ../sbt-scaladoc-settings-plugin/src/main/scaladoc/doc-files -> ../sbt-scaladoc-settings-plugin/target/scala-2.10/sbt-0.13/api/doc-files
    * @param scalaDocDir The scaladoc root path directory (e.g src/main/scaladoc/)
    * @param targetDir The API docs output directory (e.g. target/scala-2.10/sbt-0.13/api)
    * @param sourceDir The source ''dir-files'' directory
    * @return The target directory under the targetDir
    */
  private def mapToTarget(scalaDocDir:File)(targetDir:File)(sourceDir:File):File = {
    new File(sourceDir.getAbsolutePath.replace(scalaDocDir.getAbsolutePath, targetDir.getAbsolutePath))
  }

}
