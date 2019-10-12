package sgit

import java.io.{File, FileWriter}
import java.math.BigInteger
import java.security.MessageDigest

import scala.io.Source

object UtilityGit {

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


}
