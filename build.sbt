name := "sGit"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
parallelExecution in Test := false

libraryDependencies += "org.mockito" %% "mockito-scala" % "1.5.18"

lazy val sgit = (project in file("."))
  .enablePlugins(JavaAppPackaging)
