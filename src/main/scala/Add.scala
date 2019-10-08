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

  def createBlob(file: File): File = {

    // get content
    val content = Source.fromFile(file).mkString
    //transform content into sha
    val sha = sha1Transformation(content)
    //create File path + /object/hash
    val blob = new File(System.getProperty("user.dir") + "/.sgit/objects/" + sha)
    blob.createNewFile()
    //write content
    val fileWriter = new FileWriter(blob,false)
    fileWriter.write(content)
    fileWriter.close()
    //send file
    blob
  }

  def add(files: Seq[File]): String = {

    // pour tous les fichiers

          // creer le blob
    createBlob(files.head)
          // ajouter le blob a object OK fais dans create blob
          // creer le fichier index s'il existe pas et mettre nom blob + path blob
    //attention cas où Index existe deja

    val INDEX = new File(System.getProperty("user.dir") + "/.sgit/INDEX")

    if (!INDEX.exists()) {
      INDEX.createNewFile()
    }

   val fileWriter = new FileWriter(INDEX,true)

    val dirPath = System.getProperty("user.dir")
    val filePath = files.head.getPath
    val filePathFromDir = filePath.replace(dirPath,"")

    val content = Source.fromFile(files.head).mkString

    fileWriter.write(filePathFromDir+" "+ sha1Transformation(content) +"\n")
    fileWriter.close()

    //
    "files add OK"
  }

}
