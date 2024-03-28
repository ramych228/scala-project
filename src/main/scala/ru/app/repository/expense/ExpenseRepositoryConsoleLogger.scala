package ru.app.repository.expense

import cats.effect.kernel.Sync
import cats.syntax.flatMap._
import cats.syntax.functor._
import ru.app.model.{Expense, ExpenseId}
import ru.app.error.DBError
import tofu.common.Console

class ExpenseRepositoryConsoleLogger[F[_]: Sync: Console](underlying: ExpenseRepository[F])
    extends ExpenseRepository[F] {

  override def create(expense: Expense): F[Either[DBError, ExpenseId]] =
    for {
      _      <- Console[F].putStrLn(s"Creating expense: $expense")
      result <- underlying.create(expense)
      _ <- result match {
        case Right(id)   => Console[F].putStrLn(s"Created expense with id: $id")
        case Left(error) => Console[F].putStrLn(s"Failed to create expense: $error")
      }
    } yield result

  override def findAll(): F[Either[DBError, List[Expense]]] =
    for {
      _      <- Console[F].putStrLn("Fetching all expenses")
      result <- underlying.findAll()
      _      <- Console[F].putStrLn(s"Fetched ${result.map(_.size)} expenses")
    } yield result

  override def find(expenseId: ExpenseId): F[Either[DBError, Option[Expense]]] =
    for {
      _ <- Console[F].putStrLn(s"Fetching ${expenseId.value} expense")
      result <- underlying.find(expenseId)
      _ <- Console[F].putStrLn(s"Fetched expense")
    } yield result


  override def update(expenseId: ExpenseId, expense: Expense): F[Either[DBError, Unit]] =
    for {
      _      <- Console[F].putStrLn(s"Updating expense with id: ${expenseId.value}")
      result <- underlying.update(expenseId, expense)
      _ <- result match {
        case Right(_)    => Console[F].putStrLn(s"Updated expense with id: ${expenseId.value}")
        case Left(error) => Console[F].putStrLn(s"Failed to update expense: $error")
      }
    } yield result

  override def delete(expenseId: ExpenseId): F[Either[DBError, Unit]] =
    for {
      _      <- Console[F].putStrLn(s"Deleting expense with id: ${expenseId.value}")
      result <- underlying.delete(expenseId)
      _ <- result match {
        case Right(_)    => Console[F].putStrLn(s"Deleted expense with id: ${expenseId.value}")
        case Left(error) => Console[F].putStrLn(s"Failed to delete expense: $error")
      }
    } yield result
}
