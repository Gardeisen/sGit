import java.io.File

import scopt.OParser
case class Config(
                   foo: Int = -1,
                   out: File = new File("."),
                   xyz: Boolean = false,
                   libName: String = "",
                   maxCount: Int = -1,
                   verbose: Boolean = false,
                   debug: Boolean = false,
                   mode: String = "",
                   files: Seq[File] = Seq(),
                   keepalive: Boolean = false,
                   jars: Seq[File] = Seq(),
                   kwargs: Map[String, String] = Map())



object Parseur extends App {

  val builder = OParser.builder[Config]
  val parser1 = {
    import builder._
    OParser.sequence(
      programName("sGit"),
      head("sGit", "1.0"),

      cmd("init")
        .action((_, c) => c.copy(mode = "init"))
        .text("create sgit repository.")


    )
  }

  // OParser.parse returns Option[Config]
  OParser.parse(parser1, args, Config()) match {
    case Some(config) =>
      config.mode match {
        case "init" =>
          println(Init.init())

        case _ =>
          println("there's no command like this, try again")
      }
  }
}
