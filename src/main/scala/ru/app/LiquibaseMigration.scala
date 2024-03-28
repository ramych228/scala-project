package ru.app

import cats.effect.MonadCancelThrow
import cats.effect.std.Console
import cats.implicits.catsSyntaxFlatMapOps
import doobie.FC
import doobie.implicits.toConnectionIOOps
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import ru.app.module.DbModule

object LiquibaseMigration {
  def run[F[_]: MonadCancelThrow: Console](
    dbModule: DbModule[F],
    changeLog: String = "changelog.xml"
  ): F[Unit] =
    Console[F].println("Starting liquibase migration...") >>
      FC.raw { conn =>
        val resourceAccessor = new ClassLoaderResourceAccessor(getClass.getClassLoader)
        val database         = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn))
        val liquibase        = new Liquibase(changeLog, resourceAccessor, database)
        liquibase.update("")
      }.transact(dbModule.transactor) >>
      Console[F].println("Migration finished.")
}
