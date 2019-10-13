package sgit

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}

import scala.reflect.io.Directory

class UtilityGitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  before {
    //create the repo
    //Init.init()
    //create the file for test
  }
  after {
    //delete all
    //new Directory(new File(System.getProperty("user.dir") + "/.sgit")).deleteRecursively()

  }
  //TEST FOR "getContent" FUNCTION
  describe("Given a file README.md content only README ") {
    //it("Should return a string 'README'"){}
    //TODO
  }

  //TEST FOR "writeInAFile" FUNCTION
  describe("Given a file test and 'something' to write") {
    //it("Should give 'something' if we get the content"){}
    //TODO
    //it("Should give test if we get the name"){}
    //TODO

    //WARNING DON'T FORGET TO DELETE THE FILE TEST
  }


}
