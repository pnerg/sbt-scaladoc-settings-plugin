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
  * Some utilities related to file management
  *
  * @author Peter Nerg.
  */
object FileUtils {
  /**
    * Implicit class that decorates ''File'' classes
    * @param f
    */
  implicit class FileDecorator(f:File) {
    /**
      * Returns the name of the file but with the extension changed to ''.png''
      * @return The PNG file name
      */
    def asPNGFileName = f.getName.takeWhile(_ != '.')+".png"

    def relativeTo(parent:File):String = f.getAbsolutePath.replace(parent.getAbsolutePath, "")
  }


}
