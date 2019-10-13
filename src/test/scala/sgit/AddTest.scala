package sgit

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}
import sgit.UtilityGit._
import scala.reflect.io.Directory

class AddTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  val path = System.getProperty("user.dir")
  val fileTest = new File(path + "/src/test/scala/sgit/fileTest.txt")
  before {
    //create the repo
    Init.init()
    fileTest.createNewFile()
    writeInAFile(fileTest,"test")
  }
  after {
    //delete all
    new Directory(new File(System.getProperty("user.dir") + "/.sgit")).deleteRecursively()
    fileTest.delete()
  }
  describe("Given a fileTest.txt to add") {

    describe("Run the function blob") {

      it("Should create a file name 'fileTest.txt content hash' in the good repo") {
        Add.createBlob(fileTest)
        val sha = sha1Transformation(getContent(fileTest))
        new File(path+"/.sgit/objects/blob/"+sha).exists() shouldBe true
      }

      it("Should create a file content the same content of the file test.md"){
        Add.createBlob(fileTest)
        val sha = sha1Transformation(getContent(fileTest))
        getContent(new File(path+"/.sgit/objects/blob/"+sha)) shouldBe "test\n"
      }
    }

    describe("Run the function add for test.md") {
      //it("Should print unknown if file doesn't exist"){}
      //TODO
      //WARNING mock INDEX ??? Possible ?? comment ne pas ecrire dans le vrai index
      //it("Should add to a file INDEX a line : test.md and the blob associate"){}
      //TODO
    }
    describe("Run the function add for test.md which have been modified") {
      //modified the content of the file test
      //it("Should create a nwe blob"){}
      //TODO
      //it("Should transform the line in the index with the new blob"){}
      //TODO
    }

    describe("Run the function add for test.md and testTwo.md :  ie a array of folder") {
      //it("Should write two line in the INDEX"){}
      //TODO
    }
  }

}
