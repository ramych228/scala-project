package ru.app.module

import cats.effect.kernel.{Async, Sync}
import cats.syntax.functor._
import doobie.Transactor
import pureconfig.ConfigSource
import ru.app.config.DbConf

final case class DbModule[F[_]](
  transactor: Transactor[F]
)

object DbModule {
  def init[I[_]: Sync, F[_]: Async](conf: ConfigSource): I[DbModule[F]] =
    Sync[I].delay(conf.at("db").loadOrThrow[DbConf])
      .map(conf =>
        Transactor.fromDriverManager[F](
          driver = conf.driver,
          url    = conf.url,
          user   = conf.user,
          pass   = conf.password
        )
      )
      .map(DbModule[F](_))
}
