package fileSearcher

import java.io.File

/** IOObject converter
  *
  */
object FileConverter {

  /** Converts dir to DirectoryObject and file to FileObject
    *
    * @param file given file
    * @return IOObject implementation (DirectoryObject or FileObject)
    */
  def convertToIOObject(file: File): IOObject =
    if(file.isDirectory) DirectoryObject(file) else FileObject(file)
}