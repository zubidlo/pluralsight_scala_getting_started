package fileSearcher

import java.io.File

import scala.util.control.NonFatal
import scala.util.matching.Regex

/** Filter implementation
  *
  * @param filter
  */
class FilterChecker(filter: String) {

  private val filterAsRegex: Regex = filter.r

  /** Checks if content text matches the regex filter
    *
    * @param content text
    * @return true or false
    */
  def matches(content: String): Boolean = filterAsRegex findFirstMatchIn content match {
    case Some(_) => true
    case None => false
  }

  /** Finds all file names matching the filter
    *
    * @param iOObjects list of file objects
    * @return list of matched file objects
    */
  def findMatchedFiles(iOObjects: List[IOObject]): List[IOObject] =
    for(
        o <- iOObjects
        if o.isInstanceOf[FileObject]
        if matches(o.name)
    ) yield o

  /** Counts all matches in file content text
    *
    * @param file given file
    * @return number of matches against filter
    */
  def findMatchedContentCount(file: File): Int = {

    def getFilterMatchCount(content: String): Int = (filterAsRegex findAllIn content).length

    import scala.io.Source

    try {
      val fileSource = Source fromFile file

      try
        fileSource.getLines.foldLeft(0)((acc, l) => acc + getFilterMatchCount(l))
      catch {
        case NonFatal(_) => 0
      }
      finally
        fileSource.close()
    }
    catch {
      case NonFatal(_) => 0
    }
  }
}

/** FilterChecker companion object
  *
  */
object FilterChecker {

  /** factory method
    *
    * @param filter regex string
    * @return new FilterChecker
    */
  def apply(filter: String) = new FilterChecker(filter)
}