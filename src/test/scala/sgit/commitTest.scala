package sgit

import java.io.File

import org.scalatest._

import scala.reflect.io.Directory

//TESTS FOR THE FUNCTION OF TRANSFORMATION
class commitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  val path = System.getProperty("user.dir")

  after(
    new Directory(new File(path + "/.sgit")).deleteRecursively()
  )

  describe("Given an array of array of Stings") {
    val list = List("marine\\gardeisen\\3","mathieu\\4")
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
        Add.add(Seq[File](new File(path+"/README.md")))
        val index = new File(path + "/.sgit/INDEX")
        val tablePath = Commit.createTableOfPath(index)
        tablePath.isEmpty shouldBe false
        tablePath.length shouldBe 1
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }

      it("Should put src in the first element of the first array") {
        Init.init()
        Add.add(Seq[File](new File(path+"/src/main/scala/sgit/Add.scala")))
        val index = new File(path + "/.sgit/INDEX")
        val tablePath = Commit.createTableOfPath(index)
        tablePath.apply(0).apply(0) shouldBe "src"
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
      it("Should put sgit in the fourth element of the first array") {
        Init.init()
        Add.add(Seq[File](new File(path+"/src/main/scala/sgit/Add.scala")))
        val index = new File(path + "/.sgit/INDEX")
        val tablePath = Commit.createTableOfPath(index)
        tablePath.apply(0).apply(3) shouldBe "sgit"

        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }
    //TEST FOR THE "createMapIndex" FUNCTION
    /*describe("I want to get the map which associate the index to the ref blob") {
      it("Should give a map with two keys") {
        Init.init()
        Add.add(Seq[File](new File(path+path+"/README.md")))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = Commit.createMapIndex(index)
        mapIndex.knownSize shouldBe 2

        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
      it("Should return good value (the hash) for the key README.md") {
        Init.init()
        val readme = new File(path+"/README.md")
        Add.add(Seq[File](readme))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = Commit.createMapIndex(index)
        mapIndex.get("/README.md") shouldBe sha1Transformation(getContent(readme))

        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }*/
  }

  //TESTS FOR THE COMMIT
  describe("???"){
    describe("????"){
      //it("")
      //TODO
    }
  }

}
