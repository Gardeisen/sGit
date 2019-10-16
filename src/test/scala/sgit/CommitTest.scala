package sgit

import java.io.File

import org.scalatest._

import scala.reflect.io.Directory

//TESTS FOR THE FUNCTION OF TRANSFORMATION
class CommitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  val path = System.getProperty("user.dir")

  after(
    new Directory(new File(path + "/.sgit")).deleteRecursively()
  )

  describe("Given an array of array of Stings") {
    val list = List("marine\\gardeisen\\3", "mathieu\\4")
    //TEST FOR THE "deepLengthMax" FUNCTION
    describe("If I want the deepest length") {
      it("Should give 3 as result") {
        assert(Commit.deepLengthMax(list) === 3)
      }
    }
  }

  describe("Given a File, looks like the Index") {

    //TESTS FOR "createTableOfPath" function
    describe("I want to get the table of path associate ie: for one cell get a word of the path") {
      it("Should give a list of size 1") {
        Init.init()
        Add.add(Seq[File](new File(path + "/README.md")))
        val index = new File(path + "/.sgit/INDEX")
        val tablePath = Commit.createTableOfPath(index)
        tablePath.isEmpty shouldBe false
        tablePath.length shouldBe 1
        tablePath.apply(0) shouldBe "\\README.md"
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }
    //TEST FOR THE "createMapIndex" FUNCTION
    describe("I want to get the map which associate the index to the ref blob") {
      it("Should give a map with one keys") {
        Init.init()
        Add.add(Seq[File](new File(path + "/README.md")))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = Commit.createMapIndex(index)
        mapIndex.knownSize shouldBe 1
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }

      it("Should return good value (the hash) for the key README.md") {
        Init.init()
        val readme = new File(path + "/README.md")
        Add.add(Seq[File](readme))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = Commit.createMapIndex(index)

        mapIndex("\\README.md") shouldBe UtilityGit.sha1Transformation(UtilityGit.getContent(readme).mkString("\n"))

        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }
  }

  //TESTS FOR THE getLengthOfElement function
  describe("When I call the function getLengthOfElement for a String") {

    it("should return 0 for an empty String ") {
      Commit.getLengthOfElement("") shouldBe 0
    }
    it("Should return 1 for the following string") {
      Commit.getLengthOfElement("\\marine") shouldBe 1
    }
    it("should return 4 for the following String") {
      Commit.getLengthOfElement("\\marine\\gardeisen\\mathieu\\4") shouldBe 4
    }

  }

  //TESTS FOR function cutLineByLength
  describe("When I call the function cutLineByLength for the string  \\marine\\gardeisen\\mathieu\\4 ") {
    it("Should return /marine/gardeisen for size 2") {
      Commit.getLengthOfElement("\\marine\\gardeisen\\mathieu\\4") > 2 shouldBe true
      Commit.cutLineByLength("\\marine\\gardeisen\\mathieu\\4", 2) shouldBe "\\marine\\gardeisen"
    }
    it("Should return /marine/gardeisen/mathieu/4 for size 4") {
      Commit.cutLineByLength("\\marine\\gardeisen\\mathieu\\4", 4) shouldBe "\\marine\\gardeisen\\mathieu\\4"
    }
  }

  //TESTS FOR function createListByLength
  describe("When I call the createListByLength function on this list (marine/4,mathieu/gardeisen/5,6) ") {
    val list = List("\\marine\\4", "\\mathieu\\gardeisen\\5", "\\6")
    it("Should return List(marine/4,mathieu/gardeisen) for size 2") {
      Commit.createListByLength(list, 2).apply(0) shouldBe "\\marine\\4"
      Commit.createListByLength(list, 2).apply(1) shouldBe "\\mathieu\\gardeisen"
    }
    it("Should return List(mathieu/gardeisen/5) for size 3") {
      Commit.createListByLength(list, 3).apply(0) shouldBe "\\mathieu\\gardeisen\\5"
    }
    it("Should return List(marine,mathieu,6) for size 1") {
      Commit.createListByLength(list, 1).apply(0) shouldBe "\\marine"
      Commit.createListByLength(list, 1).apply(1) shouldBe "\\mathieu"
      Commit.getLengthOfElement(list.apply(2)) shouldBe 1
      Commit.createListByLength(list, 1).apply(2) shouldBe "\\6"
    }

  }

  //TESTS FOR function createMapFromList
  describe("???") {
    //it("")
  }

  //TESTS FOR THE COMMIT
  describe("???") {
    describe("????") {
      //it("")
      //TODO
    }
  }

}
