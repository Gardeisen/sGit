package sgit

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}

import scala.reflect.io.Directory

class InitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  after {
    new Directory(new File(System.getProperty("user.dir")+"/.sgit")).deleteRecursively()
  }

  //TESTS FOR THE INIT
  describe("When I run the init command") {

    Init.init()
    val path = System.getProperty("user.dir")

    it("Should create a file HEAD") {
      val head = new File(path + "/.sgit/HEAD")
      head.exists() shouldBe true
    }
    it("HEAD Should contains branches/master") {

    }

    //it("Should create a folder .sgit"){}

    //it("Should create a folder .sgit/objects"){}

    //it("Should create a folder .sgit/branches"){}

    //it("Should create a folder .sgit/tags"){}

    //it("Should create a folder .sgit/objects/blob"){}

    //it("Should create a folder .sgit/objects/tree"){}

    //it("Should create a folder .sgit/branches"){}

    //it("Should print already initialized if a repo already exist"){}

    // WARNING : delete all after !

  }

}
