import java.io.File
import Add.getContent

object Commit {


  def createTableOfPath(file : File): Array[Array[String]] ={
    val content = getContent(file)
    val split = content.split("[\n, ]")
    val splitStepTwo = split.filter(e=>split.indexOf(e)==0 ||split.indexOf(e)%2 == 0)
    splitStepTwo.map(e => e.split("""(\\)"""))
  }

  def createMapIndex(file: File): Map[String,String] = {

    val content = getContent(file)
    val split = content.split("\n")
    split.map(e => (e.split(" ").apply(0), (e.split(" ").apply(1)))).toMap

    /*for ( e <- splitStepTwo) {
      println(" [ \""+e._1+"\" : \""+e._2+"\" ]")
    }*/

  }
  def commit(): Unit = {

    val INDEX = new File(System.getProperty("user.dir") + "/.sgit/INDEX")
    //TO DO

  }

}
