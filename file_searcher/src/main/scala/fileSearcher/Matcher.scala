package fileSearcher

import java.io.File
import scala.annotation.tailrec

/** Matcher implementation
  *
  * @param filter regex string
  * @param rootPath path where to start search, default = "."
  * @param checkSubDirs true if search in subdirectories of rootPath
  * @param contentFilter Option of regex string filter to search in files content
  */
class Matcher(
               filter: String,
               val rootPath: String = new File(".").getCanonicalPath,
               checkSubDirs: Boolean = false,
               contentFilter: Option[String] = None
) {

  private val rootIOObject: IOObject = FileConverter convertToIOObject new File(rootPath)

  /** executes the search
    *
    * @return list of tuples (matched file path, count of content matches)
    */
  def execute: List[(String, Option[Int])] = {

    @tailrec
    def tailrecMatch(files: List[IOObject], acc: List[FileObject]): List[FileObject] =

      files match {
        case List() =>
          acc
        case h :: tail =>
          h match {
            case f: FileObject if FilterChecker(filter) matches f.name => tailrecMatch(tail, f :: acc)
            case d: DirectoryObject => tailrecMatch(tail ::: d.children(), acc)
            case _ => tailrecMatch(tail, acc)
          }
    }

    val matchedFiles = rootIOObject match {
      case f: FileObject if FilterChecker(filter) matches f.name => List(f)
      case d: DirectoryObject =>
        if(checkSubDirs)
          tailrecMatch(d.children(), List())
        else 
          FilterChecker(filter) findMatchedFiles d.children() 
      case _ => List()
    }

    val contentFilteredFiles = contentFilter match {
      case Some(textFilter) =>
        matchedFiles
          .map(o => (o, Some(FilterChecker(textFilter).findMatchedContentCount(o.file))))
          .filter(t => t._2.get > 0)
      case None => matchedFiles map(o => (o, None))
    }
    
    contentFilteredFiles map { case(o, c) => (o.fullName, c) }
  }
}