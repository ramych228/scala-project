import Dependencies.*

ThisBuild / scalaVersion := "2.13.11"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val config = (project in file("."))
  .settings(
    libraryDependencies ++= List(
      scalaTest,
      newtype,
      `cats-effect`,
      h2,
      logback,
      liquibase,
      quill,
      pureconfig,
      doobie.postgres,
      http4s.ember,
      tapir.http4s
    ) ++ circe.modules ++ tapir.modules ++ doobie.modules ++ sttp.modules ++ tethys.modules ++ tofu.modules ++ tofu.loggingModules,
    scalacOptions += "-Ymacro-annotations",
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  )
  .settings(Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "migrations")
