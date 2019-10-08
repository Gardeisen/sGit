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
        .text("create sgit empty repository."),

      cmd(name = "add")
        .action((_, c) => c.copy(mode = "add"))
          .text("add files to the INDEX")
          .children(arg[File]("<file>...")
            .required()
            .unbounded()
            .action((x, c) => c.copy(files = c.files :+ x))
            .text("file to add to INDEX")),


        )
  }

  // OParser.parse returns Option[Config]
  OParser.parse(parser1, args, Config()) match {
    case Some(config) =>
      config.mode match {
        case "init" =>
          println(Init.init())

        case "add" =>
          Add.add(config.files)
          "add tous les fichiers"

        case "add" => //pour

        case _ =>
          println("there's no command like this, try again")
      }
  }
}
