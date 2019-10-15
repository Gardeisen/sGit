package sgit

import java.io.File

import sgit.UtilityGit._

import scala.annotation.tailrec
import scala.io.Source

object Commit {

  /**
   * function createTableOfPath, function to simplify the rest of the function commit
   *
   * @param file : the file you want to separate into a table of words
   * @return a matrix contains the file but one word for each cell
   */
  def createTableOfPath(file: File): List[String] = {

    val buffered_reader = Source.fromFile(file)
    val content = buffered_reader.getLines().toList
    buffered_reader.close()
    content.map(line => line.split(" ").head)
  }

  /**
   * function createMapIndex, is a utility function to create parameters for the createTree function
   *
   * @param file : the file you want to transform into a Map
   * @return a Map which correspond to the file mapped
   */
  def createMapIndex(file: File): Map[String, String] = {

    val content = getContent(file)
    val split = content.split("\n")
    split.map(e => (e.split(" ").apply(0), e.split(" ").apply(1))).toMap
  }

  /**
   * Function deepLengthMax, calcul the length max of a list of list
   *
   * @param tab    : the list of list you want to know the max length
   * @param length : use to store the result for the recursion
   * @return the length maximum of the tab
   */
  @tailrec
  def deepLengthMax(tab: List[String], length: Int = 0): Int = {
    if (tab.isEmpty) {
      return length
    }
    else {
      if (tab.head.length > length) {
        deepLengthMax(tab.tail, tab.head.length)
      }
      else {
        deepLengthMax(tab.tail, length)
      }
    }
  }

  def getLengthOfElement(element: String): Int = {
    if (element.isEmpty) {
      0
    }
    else if (element.contains("\\")) {
      1
    }
    else {
      element.split(("\\")).length
    }
  }

  @tailrec
  def cutLineByLength(line: String, length: Int): String = {
    if (line.isEmpty) {
      " "
    }
    else if (getLengthOfElement(line) <= length) {
      line
    }
    else {
      cutLineByLength(line.splitAt(line.lastIndexOf("\\"))._1, length)
    }
  }

  def createListByLength(list: List[String], length: Int): List[String] = {
    list.filter(line => getLengthOfElement(line) >= length).map(line => cutLineByLength(line, length))
  }

  def createMapFromList(list: List[String], length: Int): Map[String, List[String]] = {
    list.groupMap(line => cutLineByLength(line, length - 1))(line => line)
  }

  /**
   * Function createTree
   *
   * @param children    : the list of under file that are liked
   * @param pathToWrite : the path you want to write the tree
   * @return a file which is the tree build
   */
  def createTree(children: List[String], pathToWrite: String = System.getProperty("user.dir") + "/.sgit/objects/tree"): File = {
    val content = children.mkString("\n")
    val tree = new File(pathToWrite + sha1Transformation(content))
    tree.createNewFile()
    writeInAFile(tree, content)
    tree
  }

  def createTreeFromMap(mapUse: Map[String, List[String]]): List[File] = {

    mapUse.toList.map(couple => createTree(couple._2))

  }

  def createTreesFromIndex(): File = {
    val index = new File(System.getProperty("user.dir") + "/.sgit/INDEX")
    val listIndex = createTableOfPath(index)

    @tailrec
    def createTreesFromIndexRec(length: Int, fileList: List[File] = List.empty): File = {
      if (length == 0) {
        fileList.last
      }
      else {
        createTreesFromIndexRec(length - 1, fileList ++ createTreeFromMap(createMapFromList(listIndex, length)))
      }
    }
    createTreesFromIndexRec(deepLengthMax(listIndex))
  }

  def commit(): File = {
    val lastTree = createTreesFromIndex()
    val content = getContent(lastTree)
    val commit = new File(System.getProperty("user.dir") + "/.sgit/objects/commit" + sha1Transformation(content))
    commit.createNewFile()
    writeInAFile(commit, content)
    commit
    //WARNING add the date + things in branch/HEAD ???
  }

}
