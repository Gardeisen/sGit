package sgit

import java.io.File

import sgit.UtilityGit._

import scala.annotation.tailrec

object Commit {

  /**
   * function createTableOfPath, function to simplify the rest of the function commit
   * @param file : the file you want to separate into a table of words
   * @return a matrix contains the file but one word for each cell
   */
  def createTableOfPath(file: File): Array[Array[String]] = {
    val content = getContent(file)
    val split = content.split("[ ,\n]")
    val splitStepTwo = split.filter(e => split.indexOf(e) == 0 || split.indexOf(e) % 2 == 0)
    splitStepTwo.map(e => e.split("""(\\)"""))
  }

  /**
   * function createMapIndex, is a utility function to create parameters for the createTree function
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
   * @param tab : the list of list you want to know the max length
   * @param length : use to store the result for the recursion
   * @return the length maximum of the tab
   */
  @tailrec
  def deepLengthMax(tab: Array[Array[String]], length: Int = 0): Int = {
    if (tab.isEmpty) {
      length
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

  /**
   * Function createTree
   * @param children : the list of under file that are liked
   * @param pathToWrite : the path you want to write the tree
   * @return a file which is the tree build
   */
  def createTree(children: Array[String], pathToWrite: String): File = {
    val content = children.mkString("\n")
    val tree = new File(pathToWrite + sha1Transformation(content))
    tree.createNewFile()
    writeInAFile(tree, content)
    tree
  }

  /**
   * WIP
   *
   * @param mapIndex
   * @param listTabPath
   * @param deepLength
   * @param mapParent
   * @param pathToWrite
   */
  def createTreesForAllTheIndex(mapIndex: Map[String, String], listTabPath: Array[Array[String]], deepLength: Int,
                                mapParent: Map[String, Array[String]] = Map.empty[String, Array[String]],
                                pathToWrite: String = System.getProperty("user.dir") + "/.sgit/objects/tree"): Unit = {
    //Step 0 : case stop
    if (deepLength == 1) {
      val stepOne = listTabPath.map(e => e.head).distinct
      var newMap = Map.empty[String, Array[String]]
      stepOne.foreach(
        e =>
          if (mapIndex.contains(e)) {
            newMap = newMap + ("" -> Array("blob " + mapIndex.apply(e) + " " + e))
          }
          else {
            val name = createTree(mapParent.apply(e), pathToWrite).getName
            newMap = newMap + ("" -> Array("tree " + name + " " + e))
          }
      )
      //attention regarder si on a bien pris le bon path ???
      createTree(newMap.apply(""), pathToWrite)
    }
    else {

      val newMapParent  = listTabPath
        .filter(e=> e.length==deepLength)
        .map(e=> Array(e.apply(deepLength - 2),e.apply(deepLength -1)))
        .groupBy(e => e.apply(0))

      //pas sur voir ce que ca fait
      newMapParent.foreach(
        e => createTree(e._2.apply(0),pathToWrite)
      )

    }

  }

  /**
   * WIP.....
   */
  def commit(): Unit = {

    val INDEX = new File(System.getProperty("user.dir") + "/.sgit/INDEX")
    //TO DO
    println("la longueur max =    ")
    print(deepLengthMax(createTableOfPath(INDEX)))
    //val deepLength = deepLengthMax(listTabPath) pour lancer createTreeOfIndex

    /*for ( e <- splitStepTwo) {
      println(" [ \""+e._1+"\" : \""+e._2+"\" ]")
    }*/

  }

}
