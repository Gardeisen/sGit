object Init {

  def init (): String ={
    //check if the folder .sgit already exist

    // if it already exist -> "the folder already exist"

    //else create :

         // folder .sgit/
    new java.io.File(System.getProperty("user.dir")+"/.sgit").mkdir()

         //folder .sgit/objects System.getProperty("user.dir")

        //folder .sgit/logs

        //folder .sgit/branches

        //folder .sgit/tags

        //folder .sgit/refs

        //file HEAD, refs/branch/master
    "test"
  }
}
