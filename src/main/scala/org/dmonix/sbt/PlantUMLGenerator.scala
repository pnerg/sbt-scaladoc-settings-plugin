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

import java.io.{File, FileInputStream, InputStreamReader}
import java.util.Collections

import net.sourceforge.plantuml.{BlockUmlBuilder, FileFormat, FileFormatOption, PSystemUtils}
import net.sourceforge.plantuml.preproc.Defines

/**
  * Utility methods for generating images out of PlantUML diagrams.
  *
  * @author Peter Nerg
  */
object PlantUMLGenerator {

  /**
    * Takes the provided plantUML (txt) file and renders a PNG file of it.
    *
    * The output file will be put in the provided directory keeping the original name but with the ''.png'' extension.
    * @param plantUMLFile The PlantUML text file
    * @param targetDir The output directory
    */
  def renderImage(plantUMLFile:File, targetDir:File) = {
    import scala.collection.JavaConversions._
    import FileUtils._
    val fileFormatOption = new FileFormatOption(FileFormat.PNG)
    val reader = new InputStreamReader(new FileInputStream(plantUMLFile))
    val builder = new BlockUmlBuilder(Collections.emptyList[String], null, new Defines, reader, targetDir, plantUMLFile.getAbsolutePath)
    val outFile = new File(targetDir, plantUMLFile.asPNGFileName)
    builder.getBlockUmls.foreach(block => {
      val diagram = block.getDiagram
      PSystemUtils.exportDiagrams(diagram, outFile, fileFormatOption).foreach(img => println(s"Wrote PlantUML image [$img]"))
    })
  }

}
