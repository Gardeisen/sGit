package sgit

import java.io.File
import sgit.UtilityGit._
import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}

import scala.reflect.io.Directory

class InitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  val path = System.getProperty("user.dir")
  before {
    Init.init()
  }

  after {
    new Directory(new File(path + "/.sgit")).deleteRecursively()
  }

  //TESTS FOR THE INIT
  describe("When I run the init command") {

    it("Should create a file HEAD") {
      val head = new File(path + "/.sgit/HEAD")
      head.exists() shouldBe true
    }
    it("HEAD Should contains branches/master") {
      val head = new File(path + "/.sgit/HEAD")
      getContent(head) shouldBe "branches/master"
    }

    it("Should create a folder .sgit") {
      val repo = new File(path + "/.sgit")
      repo.exists() shouldBe true
    }

    it("Should create a folder .sgit/objects") {
      val objects = new File(path + "/.sgit/objects")
      objects.exists() shouldBe true
    }

    it("Should create a folder .sgit/branches") {
      val branches = new File(path + "/.sgit/branches")
      branches.exists() shouldBe true
    }

    it("Should create a folder .sgit/tags") {
      val tags = new File(path + "/.sgit/tags")
      tags.exists() shouldBe true
    }

    it("Should create a folder .sgit/objects/blob") {
      val blob = new File(path + "/.sgit/objects/blob")
      blob.exists() shouldBe true
    }

    it("Should create a folder .sgit/objects/tree") {
      val tree = new File(path + "/.sgit/objects/tree")
      tree.exists() shouldBe true
    }

    it("Should not be true if a repo already exist") {

      val repo = new File(path + "/.sgit")
      Init.init() shouldBe "the repository already exist"
    }


  }
}
