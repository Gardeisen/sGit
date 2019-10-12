package sgit

import java.io.File

import sgit.UtilityGit._

import scala.annotation.tailrec

object Commit {


  def createTableOfPath(file: File): Array[Array[String]] = {
    val content = getContent(file)
    val split = content.split("[\n, ]")
    val splitStepTwo = split.filter(e => split.indexOf(e) == 0 || split.indexOf(e) % 2 == 0)
    splitStepTwo.map(e => e.split("""(\\)"""))
  }

  def createMapIndex(file: File): Map[String, String] = {

    val content = getContent(file)
    val split = content.split("\n")
    split.map(e => (e.split(" ").apply(0), e.split(" ").apply(1))).toMap
  }

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

  def createTree(children: Array[String], pathToWrite: String): File = {
    val content = children.mkString("\n")
    val tree = new File(pathToWrite + sha1Transformation(content))
    tree.createNewFile()
    writeInAFile(tree, content)
    tree
  }

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
