package ru.app.repository.expense

import cats.effect.kernel.MonadCancelThrow
import cats.implicits._
import doobie.implicits._
import doobie.util.transactor.Transactor
import ru.app.model.{Expense, ExpenseId}
import ru.app.error.{DBError, UnexpectedDbError}

final class ExpenseRepositoryDbImpl[F[_]: MonadCancelThrow](
  val transactor: Transactor[F]
) extends ExpenseRepository[F] {

  override def create(expense: Expense): F[Either[DBError, ExpenseId]] =
    sql"""
      INSERT INTO expenses (userId, description, amount)
      VALUES (${expense.userId}, ${expense.description}, ${expense.amount})
    """.update.withUniqueGeneratedKeys[Long]("id")
      .attempt.transact(transactor)
      .map(_.leftMap(_ => UnexpectedDbError("Error creating expense"))
        .map(id => ExpenseId(id)))

  override def find(expenseId: ExpenseId): F[Either[DBError, Option[Expense]]] = {
    sql"""
        SELECT id, userId, amount, description
        FROM expenses
        WHERE id = $expenseId
      """.query[Expense].option
      .transact(transactor)
      .attempt
      .map {
        case Right(expenseOption) => Right(expenseOption)
        case Left(ex) => Left(UnexpectedDbError("Error fetching expense: " + ex.getMessage))
      }
  }

  override def findAll(): F[Either[DBError, List[Expense]]] =
    sql"""
      SELECT id, userId, amount, description
      FROM expenses
    """.query[Expense].to[List]
      .attempt.transact(transactor)
      .map(_.leftMap(_ => UnexpectedDbError("Error fetching expenses")))

  override def update(expenseId: ExpenseId, expense: Expense): F[Either[DBError, Unit]] =
    sql"""
      UPDATE expenses
      SET description = ${expense.description}, amount = ${expense.amount}
      WHERE id = ${expenseId.value.toLong}
    """.update.run
      .attempt.transact(transactor)
      .map(_.leftMap(_ => UnexpectedDbError("Error updating expense"))
        .map(_ => ()))

  override def delete(expenseId: ExpenseId): F[Either[DBError, Unit]] =
    sql"""
      DELETE FROM expenses
      WHERE id = ${expenseId.value.toLong}
    """.update.run
      .attempt.transact(transactor)
      .map(_.leftMap(_ => UnexpectedDbError("Error deleting expense"))
        .map(_ => ()))
}
