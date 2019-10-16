package sgit

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}
import sgit.UtilityGit._
import scala.reflect.io.Directory

class UtilityGitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  val path = System.getProperty("user.dir")
  val fileTest = new File(path + "/src/test/scala/sgit/fileTest.txt")
  before {
    //create the repo
    Init.init()
  }
  after {
    //delete all
    new Directory(new File(System.getProperty("user.dir") + "/.sgit")).deleteRecursively()
    fileTest.delete()
  }

  //TEST FOR "getContent and write" FUNCTION
  describe("Given a file test.txt content only test test ") {
    it("Should return a string 'test test' when getContent is call") {
      writeInAFile(fileTest, "test test\n")
      getContent(fileTest).mkString("\n") shouldBe "test test\n"
      writeInAFile(fileTest, "encore encore")
      getContent(fileTest).mkString shouldBe "test testencore encore"
      fileTest.delete()
    }
  }


}
