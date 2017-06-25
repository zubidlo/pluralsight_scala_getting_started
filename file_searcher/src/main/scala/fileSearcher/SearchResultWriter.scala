package fileSearcher

import java.io.{FileWriter, PrintWriter}

/** Writes search results to file or console
  *
  */
object SearchResultWriter {

  private def getString(fname: String, countOpt: Option[Int]): Any =
    countOpt match {
      case Some(c) => s"\t$fname => $c"
      case None => s"\t$fname"
    }

  /** Writes to console
    *
    * @param results list of tuples
    */
  def writeToConsole(results: List[(String, Option[Int])]): Unit = {
    for((fname, countOpt) <- results) println(getString(fname, countOpt))
  }

  /** Writes to file
    *
    * @param path file to write results to
    * @param searchResults list of tuples
    */
  def writeToFile(path: String, searchResults: List[(String, Option[Int])]): Unit = {
    val fW = new FileWriter(path)
    val pW = new PrintWriter(fW)

    try
        for(
          (fname, countOpt) <- searchResults
        ) pW.println(
          countOpt match {
            case Some(c) => s"$fname -> $c"
            case None => s"$fname"
          }
        )
    finally {
      pW.close()
      fW.close()
    }
  }
}
