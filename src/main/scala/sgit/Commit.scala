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
  def createMapIndex(file: File = new File(System.getProperty("user.dir") + "/.sgit/INDEX")): Map[String, String] = {

    getContent(file).map(e => (e.split(" ").head, e.split(" ").tail.mkString)).toMap

  }

  /**
   * Function deepLengthMax, compute the length max of a list of list
   *
   * @param tab    : the list of list you want to know the max length
   * @param length : use to store the result for the recursion
   * @return the length maximum of the tab
   */
  @tailrec
  def deepLengthMax(tab: List[String], length: Int = 0): Int = {

    if (tab.isEmpty) {
      length
    }
    else if (tab.head.split("""(\\)""").length > length) {
      deepLengthMax(tab.tail, tab.head.split("""(\\)""").length)
    }
    else {
      deepLengthMax(tab.tail, length)
    }
  }


  /**
   * Function getLengthOfElement compute for the string the length as per of the \
   *
   * @param element : the string we want to compute the size
   * @return a int correspond to the size
   */
  def getLengthOfElement(element: String): Int = {
    if (element.isEmpty) {
      0
    }
    else if (element.contains("""(\\)""")) {
      1
    }
    else {
      element.split("""(\\)""").length-1
    }
  }

  /**
   * function cutLineByLength is use to segment the string as per of the \
   *
   * @param line   : the string we want to cut
   * @param length : the index, size at where we want to cut
   * @return the string cut
   */
  @tailrec
  def cutLineByLength(line: String, length: Int): String = {
    if (line.isEmpty) {
      " "
    }
    else if (getLengthOfElement(line) <= length) {
      line
    }
    else {
      cutLineByLength(line.splitAt(line.lastIndexOf('\\'))._1, length)
    }
  }

  /**
   * function createListByLength make a new list we the string of the right size
   *
   * @param list   : the global list
   * @param length : the size we want to cut
   * @return a list with the good element to the right size
   */
  def createListByLength(list: List[String], length: Int): List[String] = {
    list.filter(line => getLengthOfElement(line) >= length).map(line => cutLineByLength(line, length))
  }

  /**
   * function createMapFromList transform a list into a map of the right size
   *
   * @param list   the list we want to transform
   * @param length the size of the element we want to make
   * @return the map
   */
  def createMapFromList(list: List[String], length: Int): Map[String, List[String]] = {
    list.groupMap(line => cutLineByLength(line, length - 1))(line => cutLineByLength(line,length))
  }

  /**
   * function create tree
   *
   * @param children    the folder or the file under the element study
   * @param path        the name of the tree before hash
   * @param pathToWrite the location where the file will be write
   * @return a file correspond to the tree
   */
  def createTree(children: List[String], path: String, pathToWrite: String = System.getProperty("user.dir") + "/.sgit/objects/trees/"): File = {

    val tree = new File(pathToWrite + sha1Transformation(path))
    if (!tree.exists()) {
      tree.createNewFile()
      writeInAFile(tree,
        children.map(
          path => if (new File(System.getProperty("user.dir") + "/.sgit/objects/blobs/" + createMapIndex().get(path)).exists()) {
            "blob " + path + " " + createMapIndex()(path)
          }
          else {
            //it's a tree
            "tree " + path + " " + sha1Transformation(path)
          }).mkString("\n")
      )
    }
    tree
  }

  /**
   * function createTreeFromMap create a tree from the map passed in parameter
   *
   * @param mapUse : the map we want to turn into a tree
   * @return a list of file which correspond to the different tree
   */
  def createTreeFromMap(mapUse: Map[String, List[String]]): List[File] = {

    mapUse.toList.map(couple => createTree(couple._2, couple._1))

  }

  /**
   * function createTreesFromIndex create all the tree correspond to an Index
   *
   * @return the last tree build
   */
  def createTreesFromIndex(): File = {
    val index = new File(System.getProperty("user.dir") + "/.sgit/INDEX")
    val listIndex = createTableOfPath(index)

    @tailrec
    def createTreesFromIndexRec(length: Int, fileList: List[File] = List.empty): File = {
      if (length == 0) {
        fileList.last
      }
      else {
        createTreesFromIndexRec(length - 1, createTreeFromMap(createMapFromList(listIndex, length)))
      }
    }

    createTreesFromIndexRec(deepLengthMax(listIndex))
  }

  /**
   * function commit create the file commit contains the ref to the last tree
   *
   * @return the file correspond to the commit
   */
  def commit(): File = {
    val lastTree = createTreesFromIndex()
    val content = getContent(lastTree).mkString("\n")
    val commit = new File(System.getProperty("user.dir") + "/.sgit/objects/commits/" + sha1Transformation(content))
    commit.createNewFile()
    writeInAFile(commit, content)
    commit
    //WARNING add the date + things in branch/HEAD ???
  }

}
