package fileSearcher

import org.scalatest.FlatSpec
import java.io.File


/**
  *
  */
class MatcherTests extends FlatSpec {

  val testFilesRootPath = new File(".").getCanonicalPath + "/testfiles"

  "Matcher that is passed a file matching the filter" should
  "return a list with that file name" in {
    val matcher = new Matcher("fake", "/fakePath")
    assert(matcher.execute == List(("/fakePath", None)))
  }
  
  "Matcher using a directory containing one file matching the filter" should
  "return a list with file name" in {
    val path = new File("./testfiles").getCanonicalPath
    val matcher = new Matcher("txt", path)
    assert(matcher.execute == List((s"${testFilesRootPath}/readme.txt", None)))
  }
  
  "Matcher that is not passed a root file location" should
  "use the current location" in {
    val pwd = new File(".").getCanonicalPath
    assert(new Matcher("filter").rootPath == pwd)
  }
  
  "Matcher with sub folder checking matching a root location with two subtree files matching" should
  "return a list with those file names" in {
    val searchSubDirectories = true
    val path = new File("./testfiles").getCanonicalPath
    val matcher = new Matcher("txt", path, searchSubDirectories)
    assert(matcher.execute == List((s"${testFilesRootPath}/morefiles/notes.txt", None), (s"${testFilesRootPath}/readme.txt", None)))
  }

  "Matcher given a path that has one file that matches file filter and content filter" should
  "return a list with file name" in {
    val matcher = new Matcher("data", new File(".").getCanonicalPath, true, Some("pluralsight"))
    assert(matcher.execute == List((s"${testFilesRootPath}/pluralsight.data", Some(3))))
  }

  "Matcher given a path that has no file that matches file filter and content filter" should
  "return an empty list" in {
    val matcher = new Matcher("txt", new File(".").getCanonicalPath, true, Some("pluralsight"))
    assert(matcher.execute == List())
  }
}