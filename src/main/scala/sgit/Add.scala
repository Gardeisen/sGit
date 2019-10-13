package sgit

import java.io.{File, FileWriter}
import sgit.UtilityGit._
object Add {

  /**
   *
   * Function create blob
   * @param file : the file you want to add after
   * @return a file, which is the blob of the file you want to add
   */
  def createBlob(file: File): File = {

    // get content
    val content = getContent(file)
    //transform content into sha
    val sha = sha1Transformation(content)
    //create File path + /object/hash
    val blob = new File(System.getProperty("user.dir") + "/.sgit/objects/blob/" + sha)
    blob.createNewFile()
    //write content
    writeInAFile(blob, content)
    //send file
    blob
  }

  /**
   * function addOneFile, add only one file
   * @param file : the file you want to add
   * The aim is to add the file to the index and to create a blob
   */
  def addOneFile(file : File) : Unit  ={
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

  /**
   * function add for all files
   * @param files : a list of files you want to add
   * Execute the function addOneFile for list
   */
  def add(files: Seq[File]) : Unit = {

    if (files.nonEmpty){
      //FAIRE UNE FONCTION POUR VOIR SI C'EST UN POINT + FONCTION ???
      addOneFile(files.head)
      add(files.tail)
    }
  println("the files have been added successfully")
  }


}
