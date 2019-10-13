package sgit

import org.scalatest.{BeforeAndAfter, FunSpec, GivenWhenThen, Matchers}

class AddTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {


  describe("Given a file test.md to add") {
    //create the file test

    describe("Run the function blob"){
      //it("Should create a file name 'test.md content hash' "){}
      //TODO
      //it("Should create a file content the same content of the file test.md"){}
    }

    describe("Run the function add for test.md"){
      //it("Should print unknown if file doesn't exist"){}
      //TODO
      //WARNING mock INDEX ??? Possible ?? comment ne pas ecrire dans le vrai index
      //it("Should add to a file INDEX a line : test.md and the blob associate"){}
      //TODO
    }
    describe("Run the function add for test.md which have been modified"){
      //modified the content of the file test
      //it("Should create a nwe blob"){}
      //TODO
      //it("Should transform the line in the index with the new blob"){}
      //TODO
    }

    describe("Run the function add for test.md and testTwo.md :  ie a array of folder"){
      //it("Should write two line in the INDEX"){}
      //TODO
    }
  }

}
