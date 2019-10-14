package sgit

import java.io.{BufferedReader, File, FileReader, FileWriter}
import java.math.BigInteger
import java.security.MessageDigest

import scala.io.Source

object UtilityGit {

  /**
   * Function sha1Transformation
   *
   * @param s : the String you want to hash
   * @return a String correspond to the param hash
   */
  def sha1Transformation(s: String): String = {
    val messDirect = MessageDigest.getInstance("SHA1")
    val digestMess = messDirect.digest(s.getBytes)
    val number = new BigInteger(1, digestMess)
    val stringHash = number.toString(16)
    stringHash

  }

  /**
   * Function getContent
   *
   * @param file : the file you want to get the content
   * @return a String correspond to the content
   */
  def getContent(file: File): String = {
    val buffered_reader = Source.fromFile(file)
    val content = buffered_reader.getLines().mkString("\n")
    buffered_reader.close()
    content
  }

  /**
   * Function WriteInAFile
   *
   * @param file    : the file you want to write in
   * @param content : the content you want to write
   */
  def writeInAFile(file: File, content: String) {
    //warning, param append = true correspond to the fact that we don't want overwrite the file just write at the end
    val fileWriter = new FileWriter(file, true)
    fileWriter.write(content)
    fileWriter.close()
  }


}
