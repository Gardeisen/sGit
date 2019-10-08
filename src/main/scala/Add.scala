import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


object Add {

  def sha1(s: String): String = {
    val messDirect = MessageDigest.getInstance("SHA1")
    val digestMess = messDirect.digest(s.getBytes)
    val number = new BigInteger(1, digestMess)
    val stringHash = number.toString( 16)
    stringHash

  }

  def createBlob(path: Seq[File]): String = {


    "on creer pas le blob nous?"
  }

  def add(path: Seq[File]): Unit = {

  }

}
