package sgit

import java.io.File

import org.mockito.IdiomaticMockito
import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}
import sgit.UtilityGit._

import scala.reflect.io.Directory

class AddTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter with IdiomaticMockito {

  val path = System.getProperty("user.dir")
  val fileTest = new File(path + "/src/test/scala/sgit/fileTest.txt")
  before {
    //create the repo
    Init.init()
    fileTest.createNewFile()
    writeInAFile(fileTest, "test")
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
        val sha = sha1Transformation(getContent(fileTest).mkString("\n"))
        new File(path + "/.sgit/objects/blobs/" + sha).exists() shouldBe true
      }

      it("Should create a file content the same content of the fileTest.txt") {
        Add.createBlob(fileTest)
        val sha = sha1Transformation(getContent(fileTest).mkString("\n"))
        getContent(new File(path + "/.sgit/objects/blobs/" + sha)).mkString("\n") shouldBe "test"
      }
    }

    describe("Run the function add for fileTest.txt") {
      it("Should not create blob if file doesn't exist") {
        val unKnowFile = new File("nowhere")
        Add.addOneFile(unKnowFile)
        new File(path + "/.sgit/objects/blobs/" + sha1Transformation(getContent(fileTest).mkString("\n"))).exists() shouldBe false
      }
      it("Should add to a file INDEX a line : fileTest.txt and the blob associate") {
        Add.addOneFile(fileTest)
        val index = new File(path + "/.sgit/INDEX")
        getContent(index).mkString("\n").contains("fileTest.txt " + sha1Transformation(getContent(fileTest).mkString("\n"))) shouldBe true
      }

    }

    describe("Run the function add for  fileTest.txt which have been modified") {
      writeInAFile(fileTest, "something more")
      it("Should create a new blob") {
        Add.addOneFile(fileTest)
        new File(path + "/.sgit/objects/blobs/" + sha1Transformation(getContent(fileTest).mkString("\n"))).exists() shouldBe true
      }

      it("Should transform the line in the index with the new blob") {
        Add.addOneFile(fileTest)
        val index = new File(path + "/.sgit/INDEX")
        getContent(index).mkString("\n").contains("fileTest.txt " + sha1Transformation(getContent(fileTest).mkString("\n"))) shouldBe true
      }
    }

    describe("Run the function add for fileTest.txt and fileTestTwo.txt :  ie a array of folder") {
      it("Should write two line in the INDEX"){
        val fileTestTwo = new File(path + "/src/test/scala/sgit/fileTestTwo.txt")
        fileTestTwo.createNewFile()
        writeInAFile(fileTestTwo, "testTwo")
        Add.add(Seq[File](fileTest,fileTestTwo))
        val index = new File(path + "/.sgit/INDEX")
        getContent(index).mkString("\n").contains("fileTest.txt " + sha1Transformation(getContent(fileTest).mkString("\n"))) shouldBe true
        getContent(index).mkString("\n")contains("fileTestTwo.txt " + sha1Transformation(getContent(fileTestTwo).mkString("\n"))) shouldBe true
        fileTestTwo.delete()
        fileTest.delete()
      }
      it("Should create two blob"){
        val fileTestTwo = new File(path + "/src/test/scala/sgit/fileTestTwo.txt")
        fileTestTwo.createNewFile()
        writeInAFile(fileTestTwo, "testTwo")
        Add.add(Seq[File](fileTest,fileTestTwo))
        new File(path + "/.sgit/objects/blobs/" + sha1Transformation(getContent(fileTest).mkString("\n"))).exists() shouldBe true
        new File(path + "/.sgit/objects/blobs/" + sha1Transformation(getContent(fileTestTwo).mkString("\n"))).exists() shouldBe true
        fileTestTwo.delete()
        fileTest.delete()
      }

    }
  }

}
