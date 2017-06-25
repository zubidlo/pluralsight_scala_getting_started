package fileSearcher

/** Main application
  *
  */
object Runner extends App {

  def toBool(boolStr: String): Boolean = List("true", "t") contains boolStr.toLowerCase

  var matcher = args match {
    case Array(filter) => new Matcher(filter)
    case Array(filter, rootPath) => new Matcher(filter, rootPath)
    case Array(filter, rootPath, checkSubDirs) => new Matcher(filter, rootPath, toBool(checkSubDirs))
    case Array(filter, rootPath, checkSubDirs, contentFilter) => new Matcher(filter, rootPath, toBool(checkSubDirs), Some(contentFilter))
  }

  val results = matcher.execute

  println(s"Found ${results.length} matches:")
  args match {
    case Array(_, _, _, _, filePath, _*) => {
      SearchResultWriter.writeToFile(filePath, results)
      println(s"Results written to $filePath")
    }
    case _ => SearchResultWriter.writeToConsole(results)
  }
}
