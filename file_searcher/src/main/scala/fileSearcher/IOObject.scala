package fileSearcher

import java.io.File

import scala.util.control.NonFatal

/** IOObject trait
  *
  */
sealed trait IOObject {
  val file: File
  val name: String = file.getName
  val fullName:String = try file.getAbsolutePath catch { case NonFatal(_) => name }
}

/** File object implementation
  *
  * @param file file
  */
sealed case class FileObject(file: File) extends IOObject

/** Directory object implementation
  *
  * @param file directory file
  */
sealed case class DirectoryObject(file: File) extends IOObject {

  /** All files and directories in directory
    *
    * @return list of IOObjects
    */
  def children(): List[IOObject] =
    try
      file.listFiles.toList map(f => FileConverter convertToIOObject f)
    catch {
      case _ : NullPointerException => List()
    }
}