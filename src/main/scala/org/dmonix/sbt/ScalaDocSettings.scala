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


/**
  * Settings to use when configuring the SBT build file building Scala Doc.

  * @author Peter Nerg
  */
object ScalaDocSettings {

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
