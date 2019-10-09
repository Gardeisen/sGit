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
    val fileWriter = new FileWriter(file, false)
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

  def add(files: Seq[File]) {

    for (file <- files) {
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

      //write the line in the INDEX
      writeInAFile(INDEX, filePathFromDir + " " + sha1Transformation(getContent(file)) + "\n")

    }
    //WARNING CASE BLOB ALREADY EXIST ISN'T HANDLE
  }

}
