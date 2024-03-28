package ru.app.repository.expense

import cats.effect.kernel.Sync
import ru.app.error.DBError
import ru.app.module.DbModule
import tofu.common.Console
import ru.app.model.{Expense, ExpenseId}

trait ExpenseRepository[F[_]] {
  def create(expense: Expense): F[Either[DBError, ExpenseId]]
  def findAll(): F[Either[DBError, List[Expense]]]
  def find(expenseId: ExpenseId): F[Either[DBError, Option[Expense]]]
  def update(expenseId: ExpenseId, expense: Expense): F[Either[DBError, Unit]]
  def delete(expenseId: ExpenseId): F[Either[DBError, Unit]]
}

object ExpenseRepository {
  def apply[F[_]: Sync: Console](
    dbModule: DbModule[F]
  ): ExpenseRepository[F] =
    new ExpenseRepositoryConsoleLogger(
      new ExpenseRepositoryDbImpl[F](dbModule.transactor)
    )
}
