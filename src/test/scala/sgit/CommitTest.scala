package sgit

import java.io.File

import org.scalatest._
import sgit.Commit.{commit, createListByLength, createMapFromList, createMapIndex, createTableOfPath, createTree, createTreeFromMap, createTreesFromIndex, cutLineByLength, deepLengthMax, getBranch, getLengthOfElement}
import sgit.UtilityGit.{getContent, sha1Transformation}

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
        assert(deepLengthMax(list) === 3)
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
        val tablePath = createTableOfPath(index)
        tablePath.isEmpty shouldBe false
        tablePath.length shouldBe 1
        tablePath.apply(0) shouldBe "README.md"
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }
    //TEST FOR THE "createMapIndex" FUNCTION
    describe("I want to get the map which associate the index to the ref blob") {
      it("Should give a map with one keys") {
        Init.init()
        Add.add(Seq[File](new File(path + "/README.md")))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = createMapIndex(index)
        mapIndex.knownSize shouldBe 1
        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }

      it("Should return good value (the hash) for the key README.md") {
        Init.init()
        val readme = new File(path + "/README.md")
        Add.add(Seq[File](readme))
        val index = new File(path + "/.sgit/INDEX")
        val mapIndex = createMapIndex(index)

        mapIndex("README.md") shouldBe UtilityGit.sha1Transformation(UtilityGit.getContent(readme).mkString("\n"))

        new Directory(new File(path + "/.sgit")).deleteRecursively()
      }
    }
  }

  //TESTS FOR THE getLengthOfElement function
  describe("When I call the function getLengthOfElement for a String") {

    it("should return 0 for an empty String ") {
      getLengthOfElement("") shouldBe 0
    }
    it("Should return 1 for the following string") {
      getLengthOfElement("marine") shouldBe 1
    }
    it("should return 4 for the following String") {
      getLengthOfElement("marine\\gardeisen\\mathieu\\4") shouldBe 4
    }

  }

  //TESTS FOR function cutLineByLength
  describe("When I call the function cutLineByLength for the string  marine\\gardeisen\\mathieu\\4 ") {
    it("Should return /marine/gardeisen for size 2") {
      getLengthOfElement("marine\\gardeisen\\mathieu\\4") > 2 shouldBe true
      cutLineByLength("marine\\gardeisen\\mathieu\\4", 2) shouldBe "marine\\gardeisen"
    }
    it("Should return /marine/gardeisen/mathieu/4 for size 4") {
      cutLineByLength("marine\\gardeisen\\mathieu\\4", 4) shouldBe "marine\\gardeisen\\mathieu\\4"
    }
  }

  //TESTS FOR function createListByLength
  describe("When I call the createListByLength function on this list (marine/4,mathieu/gardeisen/5,6) ") {
    val list = List("marine\\4", "mathieu\\gardeisen\\5", "6")
    it("Should return List(marine/4,mathieu/gardeisen) for size 2") {
      createListByLength(list, 2).apply(0) shouldBe "marine\\4"
      createListByLength(list, 2).apply(1) shouldBe "mathieu\\gardeisen"
    }
    it("Should return List(mathieu/gardeisen/5) for size 3") {
      createListByLength(list, 3).apply(0) shouldBe "mathieu\\gardeisen\\5"
    }
    it("Should return List(marine,mathieu,6) for size 1") {
      createListByLength(list, 1).apply(0) shouldBe "marine"
      createListByLength(list, 1).apply(1) shouldBe "mathieu"
      getLengthOfElement(list.apply(2)) shouldBe 1
      createListByLength(list, 1).apply(2) shouldBe "6"
    }

  }

  //TESTS FOR function createMapFromList
  describe("When I call the function createMapFromList on this list (marine/4,mathieu/gardeisen/5,6) ") {
    val list = List("marine\\4", "mathieu\\gardeisen\\5", "6")

    it("Should for size 2 return the map : /marine -> 4 , /mathieu -> gardeisen") {
      createMapFromList(list, 2)("marine").apply(0) shouldBe "marine\\4"
      createMapFromList(list, 2)("mathieu").apply(0) shouldBe "mathieu\\gardeisen"
    }

    it("Should for size 1 return the map :   -> marine,mathieu,6 ") {
      createMapFromList(list, 1)(" ").apply(0) shouldBe "marine"
      createMapFromList(list, 1)(" ").apply(1) shouldBe "mathieu"
      createMapFromList(list, 1)(" ").apply(2) shouldBe "6"
    }
    it("Should for size 3 return the map : mathieu/gardeisen -> mathieu/gardeisen/5 ") {
      createMapFromList(list, 3)("mathieu\\gardeisen").apply(0) shouldBe "mathieu\\gardeisen\\5"
    }
  }

  //TESTS FOR function create tree
  describe("When I call the function createTree(L(src/main/scala/sgit/Add.scala,src/main/scala/sgit/Init.scala),src/main/scala/sgit)") {

    it("Should create a file sha(src/main/scala/sgit)") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      Add.add(Seq[File](add, init))
      createTree(List("src\\main\\scala\\sgit\\Init.scala", "src\\main\\scala\\sgit\\Add.scala"), "src\\main\\scala\\sgit").exists() shouldBe true
    }
    it("Should contains the different blob and their hash ") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      Add.add(Seq[File](add, init))
      getContent(
        createTree(
          List("src\\main\\scala\\sgit\\Init.scala", "src\\main\\scala\\sgit\\Add.scala"), "src\\main\\scala\\sgit")
      ).mkString("\n").contains("src\\main\\scala\\sgit\\Init.scala") shouldBe true
    }

    it("Should contains the tree and their hash ") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      Add.add(Seq[File](add))
      getContent(
        createTree(
          List("src\\main\\scala\\sgit"), "src\\main\\scala")
      ).mkString("\n") shouldBe "tree src\\main\\scala\\sgit " + sha1Transformation("src\\main\\scala\\sgit")
    }

    new Directory(new File(path + "/.sgit")).deleteRecursively()

  }

  //TESTS FOR function createTreeFromMap
  describe("When I call the function on a map src/main/scala/sgit -> (Add.scala, Init.scala)") {
    it("Should build a tree name sha(src/main/scala/sgit)") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      Add.add(Seq[File](add, init))
      val map = Map("src\\main\\scala\\sgit" -> List("src\\main\\scala\\sgit\\Add.scala", "src\\main\\scala\\sgit\\Init.scala"))
      createTreeFromMap(map)
      new File(path + "/.sgit/objects/trees/" + sha1Transformation("src\\main\\scala\\sgit")).exists() shouldBe true

      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
  }

  //TESTS FOR function createTreesFromIndex
  describe("When I run the function createTreesFromIndex") {
    it("Should create a tree root, scr,main,scala,sgit") {
      Init.init()
      val index = new File(path + "/.sgit/INDEX")
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      val readme = new File(path + "/README.md")
      Add.add(Seq[File](add, init, readme))
      createTreesFromIndex(index)

      new File(path + "/.sgit/objects/trees/" + sha1Transformation("src\\main\\scala\\sgit")).exists() shouldBe true
      new File(path + "/.sgit/objects/trees/" + sha1Transformation("src\\main\\scala")).exists() shouldBe true
      new File(path + "/.sgit/objects/trees/" + sha1Transformation("src\\main")).exists() shouldBe true
      new File(path + "/.sgit/objects/trees/" + sha1Transformation("src")).exists() shouldBe true
      new File(path + "/.sgit/objects/trees/" + sha1Transformation(" ")).exists() shouldBe true

      getContent(new File(path + "/.sgit/objects/trees/" + sha1Transformation("src\\main\\scala"))).mkString("\n").contains(sha1Transformation("src\\main\\scala\\sgit")) shouldBe true
      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
  }

  //TEST for function commit
  describe("When I run the function commit") {
    it("Should create an object commit in the right place") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      val readme = new File(path + "/README.md")
      Add.add(Seq[File](add, init, readme))
      commit().exists() shouldBe true
      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
    it("Should write in the actual branch file the new commit") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      val readme = new File(path + "/README.md")
      Add.add(Seq[File](add, init, readme))
      val commit = Commit.commit()
      getContent(new File(path + "/.sgit/branches" + getBranch)).mkString shouldBe commit.getName
      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
    it("Should write an other commit with a parent if it's not the first commit") {
      Init.init()
      val add = new File(path + "/src/main/scala/sgit/Add.scala")
      val init = new File(path + "/src/main/scala/sgit/Init.scala")
      Add.add(Seq[File](add, init))
      val firstCommit = Commit.commit()
      val readme = new File(path + "/README.md")
      Add.add(Seq[File](readme))
      val secondCommit = Commit.commit()
      getContent(new File(path + "/.sgit/branches" + getBranch)).mkString shouldBe secondCommit.getName
      new File(path + "/.sgit/commits/" + secondCommit.getName).exists() shouldBe true
      //getContent(new File(path + "/.sgit/commits/" + secondCommit.getName)).mkString.contains(firstCommit.getName) shouldBe true
      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
  }

  //TEST for function getBranch
  describe("When I use de function get Branch ") {
    it("Should return master") {
      Init.init()
      getBranch shouldBe "/master"
      new Directory(new File(path + "/.sgit")).deleteRecursively()
    }
  }
}
