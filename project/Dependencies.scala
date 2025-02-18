import Dependencies.tapir.tapirVersion
import sbt.*

object Dependencies {
  val scalaTest     = "org.scalatest" %% "scalatest" % "3.2.17" % Test
  val newtype       = "io.estatico" %% "newtype" % "0.4.4"
  val `cats-core`   = "org.typelevel" %% "cats-core" % "2.9.0"
  val `cats-effect` = "org.typelevel" %% "cats-effect" % "3.5.2"
  val h2            = "com.h2database" % "h2" % "2.1.214"
  val logback       = "ch.qos.logback" % "logback-classic" % "1.4.7"
  val liquibase     = "org.liquibase" % "liquibase-core" % "4.20.0"
  val quill         = "io.getquill" %% "quill-doobie" % "4.8.0"
  val pureconfig    = "com.github.pureconfig" %% "pureconfig" % "0.17.4"

  object circe {
    val circeVersion = "0.14.5"
    val modules: List[ModuleID] = List(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion
    )
  }

  object tethys {
    val tethysVersion = "0.26.0"
    val modules: List[ModuleID] = List(
      "com.tethys-json" %% "tethys" % tethysVersion,
      "com.tethys-json" %% "tethys-derivation" % tethysVersion
    )
  }

  object tapir {
    val tapirVersion = "1.9.0"
    val modules: List[ModuleID] = List(
      "com.softwaremill.sttp.tapir" %% "tapir-cats-effect" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "com.softwaremill.sttp.tapir" %% "tapir-json-tethys" % tapirVersion,
      "com.softwaremill.sttp.client3" %% "circe" % "3.9.1" % Test
    )

    val vertex = "com.softwaremill.sttp.tapir" %% "tapir-vertx-server-cats" % tapirVersion
    val http4s = "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion
  }

  object http4s {
    val http4sVersion = "0.23.24"

    val ember = "org.http4s" %% "http4s-ember-server" % http4sVersion
  }

  object sttp {
    val versionSttp = "3.8.15"
    val modules: List[ModuleID] = List(
      "com.softwaremill.sttp.client3" %% "core" % versionSttp,
      "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % versionSttp,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % tapirVersion
    )
  }

  object doobie {
    val doobieVersion = "1.0.0-RC2"
    val modules: List[ModuleID] = List(
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-h2" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion
    )

    val postgres = "org.tpolecat" %% "doobie-postgres" % doobieVersion
  }

  object tofu {
    val tofuVersion = "0.12.0.1"
    val modules: List[ModuleID] = List(
      "tf.tofu" %% "tofu-core-ce3" % tofuVersion,
      "tf.tofu" %% "tofu-kernel" % tofuVersion
    )

    val loggingModules: List[ModuleID] = List(
      "tf.tofu" %% "tofu-logging" % tofuVersion,
      "tf.tofu" %% "tofu-logging-derivation" % tofuVersion,
      "tf.tofu" %% "tofu-logging-layout" % tofuVersion,
      "tf.tofu" %% "tofu-logging-logstash-logback" % tofuVersion,
      "tf.tofu" %% "tofu-logging-structured" % tofuVersion
    )
  }
}
