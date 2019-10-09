import java.io.{File}
import Add.writeInAFile

object Init {

  def init(): String = {
    //check if the folder .sgit already exist

    if (!new File(System.getProperty("user.dir") + "/.sgit").exists()) {
      // folder .sgit/
      new File(System.getProperty("user.dir") + "/.sgit").mkdir()
      //folder .sgit/objects
      new File(System.getProperty("user.dir") + "/.sgit/objects").mkdir()
      //folder .sgit/logs : create later with first commit
      //new File(System.getProperty("user.dir") + "/.sgit/logs").mkdir()
      //folder .sgit/branches
      new File(System.getProperty("user.dir") + "/.sgit/branches").mkdir()
      //folder .sgit/tags
      new File(System.getProperty("user.dir") + "/.sgit/tags").mkdir()

      //file HEAD, refs/branch/master
      val head = new File(System.getProperty("user.dir") + "/.sgit/HEAD")
      head.createNewFile()
      //write content
      writeInAFile(head,"branches/master")
      ".sgit initialized"
    }
    // if it already exist -> "the folder already exist"
    else {
      "the repository already exist"
    }

  }
}
