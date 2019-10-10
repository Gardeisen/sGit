import java.io.{File, FileWriter}
import java.math.BigInteger
import java.security.MessageDigest
import scala.io.Source

object Add {

  def sha1Transformation(s: String): String = {
    val messDirect = MessageDigest.getInstance("SHA1")
    val digestMess = messDirect.digest(s.getBytes)
    val number = new BigInteger(1, digestMess)
    val stringHash = number.toString(16)
    stringHash

  }

  def getContent(file: File): String = {
    Source.fromFile(file).mkString
  }

  def writeInAFile(file: File, content: String) {
    val fileWriter = new FileWriter(file, true)
    fileWriter.write(content)
    fileWriter.close()
  }


  def createBlob(file: File): File = {

    // get content
    val content = getContent(file)
    //transform content into sha
    val sha = sha1Transformation(content)
    //create File path + /object/hash
    val blob = new File(System.getProperty("user.dir") + "/.sgit/objects/" + sha)
    blob.createNewFile()
    //write content
    writeInAFile(blob, content)
    //send file
    blob
  }

  def addOneFile(file : File) {
    if (file.exists()){
      // create blob
      createBlob(file)
      //get the INDEX or create it

      val INDEX = new File(System.getProperty("user.dir") + "/.sgit/INDEX")
      if (!INDEX.exists()) {
        INDEX.createNewFile()
      }
      //get the different directory
      val dirPath = System.getProperty("user.dir")
      val filePath = file.getPath
      val filePathFromDir = filePath.replace(dirPath, "")

      //Case where it's a file which have been modified
      if(getContent(INDEX).contains(filePathFromDir)){
        val newIndexList = getContent(INDEX).split("\n").filter(newIndexList => !newIndexList.contains(filePathFromDir))
        val newIndex = newIndexList.mkString("\n")
        val fileWriter = new FileWriter(INDEX, false)
        fileWriter.write(newIndex+ "\n"+ filePathFromDir + " " + sha1Transformation(getContent(file)) + "\n")
        fileWriter.close()
      }
      else {
        //write the line in the INDEX
        writeInAFile(INDEX, filePathFromDir + " " + sha1Transformation(getContent(file)) + "\n")
      }

    }
    else {
      println(file.getName+ "  : file unknown")
    }

  }

  def add(files: Seq[File]) : String = {

    if (files.nonEmpty){
      //FAIRE UNE FONCTION POUR VOIR SI C'EST UN POINT + FONCTION ???
      addOneFile(files.head)
      add(files.tail)
    }
  "the files have been added successfully"
  }


}
