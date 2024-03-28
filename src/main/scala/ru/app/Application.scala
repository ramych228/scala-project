package ru.app

import cats.effect.kernel.Sync
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{IpLiteralSyntax, Port}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import pureconfig.ConfigSource
import ru.app.config.ServerConf
import ru.app.controller.ExpenseController
import ru.app.module.DbModule
import ru.app.repository.expense.ExpenseRepository
import ru.app.service.ExpenseServiceImpl
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.SwaggerUI
import tofu.logging.Logging

// docker run --name my-postgres -e POSTGRES_PASSWORD=password -e POSTGRES_USER=user -e POSTGRES_DB=mydb -dasd -p 5432:5432 postgres:alpine
object Application
    extends IOApp {

  private type Init[A] = IO[A]
  private type App[A]  = IO[A]

  private val logger = Logging.Make.plain[IO].forService[Application.type]

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      _ <- logger.info("Starting service....")
      conf = ConfigSource.default
      server   <- Sync[Init].delay(conf.at("server").loadOrThrow[ServerConf])
      dbModule <- DbModule.init[Init, App](conf)
      _        <- LiquibaseMigration.run(dbModule)

      expenseRepository = ExpenseRepository[App](dbModule)
      expenseService    = new ExpenseServiceImpl[App](expenseRepository)
      expenseController = new ExpenseController(expenseService)

      openApi = OpenAPIDocsInterpreter()
        .toOpenAPI(
          es      = expenseController.apiEndpoints.map(_.endpoint),
          title   = "Application API",
          version = "1.0"
        )
        .toYaml

      routes = Http4sServerInterpreter[IO]().toRoutes(
          expenseController.apiEndpoints ++
          SwaggerUI[IO](openApi)
      )
      httpApp = Router("/" -> routes).orNotFound

      service: EmberServerBuilder[IO] = EmberServerBuilder
        .default[IO]
        .withPort(Port.fromInt(server.port).getOrElse(port"8080"))
        .withHttpApp(httpApp)
    } yield service)
      .flatMap(_.build.useForever)
      .as(ExitCode.Success)
}
