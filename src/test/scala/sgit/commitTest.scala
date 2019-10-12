package sgit

import java.io.File

import org.scalatest._
import sgit.UtilityGit._

//TESTS FOR THE FUNCTION OF TRANSFORMATION
class commitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  describe("Given an array of array of Stings") {
    val list = Array(Array("marine", "gardeisen", "3"), Array("mathieu", "gardeisen", "4"), Array("2"), Array("marine", "4"))
    //TEST FOR THE "deepLengthMax" FUNCTION
    describe("If I want the deepest length") {
      it("Should give 3 as result") {
        assert(Commit.deepLengthMax(list) === 3)
      }
    }
  }

  describe("Given a File, looks like the Index") {

    val INDEX = new File(System.getProperty("user.dir") + "/src/test/scala/sgit/indexTest")
    val content = "README.md 564z16z328fe42fz6 \n src/main/scala/sgit/Add.scala ferg555zz5fr95vz64f869er8z"
    INDEX.createNewFile()
    writeInAFile(INDEX, content)

    //TESTS FOR "createTableOfPath" function
    describe("I want to get the table of path associate ie: for one cell get a word of the path") {
      it("Should give a list of size 2") {
        //TO DO
      }
      it("Should put README.md in the first element of the first array") {
        //TO DO
      }
      it("Should put main in the second element of the second array") {
        //TO DO
      }
    }
    //TEST FOR THE "createMapIndex" FUNCTION
    describe("I want to get the map which associate the index to the ref blob") {
      it("Should give a map with two keys") {
        //TO DO
      }
      it("Should return 564z16z328fe42fz6 for the key README.md") {
        //TO DO
      }
    }

    //delete the file test
    INDEX.deleteOnExit()
  }

  //TESTS FOR THE COMMIT
  describe("???"){
    describe("????"){
      //it("")
      //TO DO
    }
  }

}
