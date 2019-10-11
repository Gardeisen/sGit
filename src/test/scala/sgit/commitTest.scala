package sgit

import org.scalatest._

class commitTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfter {

  describe("Given an array of array of Stings") {
    val list = Array( Array("matthieu", "dye", "3") , Array("matthieu", "marie", "4") , Array("2"), Array("marine", "4"))

    describe("If I want the deepest length") {
      it("TMax length is 3") {
        assert(Commit.deepLengthMax(list)===3)
      }


    }
  }

}
