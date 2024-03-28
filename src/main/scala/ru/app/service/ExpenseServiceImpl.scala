package ru.app.service

import cats.Monad
import cats.implicits._
import ru.app.error.{ApiError, DBError, ServerApiError, UnauthorizedApiError, UnexpectedDbError}
import ru.app.model.{Expense, ExpenseId}
import ru.app.repository.expense.ExpenseRepository
import tofu.syntax.feither._

class ExpenseServiceImpl[F[_]: Monad](
  expenseRepository: ExpenseRepository[F]
) extends ExpenseService[F] {

  override def listExpenses(): F[Either[ApiError, List[Expense]]] =
    expenseRepository
      .findAll()
      .leftMapIn(fromDBError)

  override def readExpense(expenseId: ExpenseId): F[Either[ApiError, Option[Expense]]] =
    expenseRepository
      .find(expenseId)
      .leftMapIn(fromDBError)

  override def addExpense(expense: Expense): F[Either[ApiError, ExpenseId]] =
    expenseRepository
      .create(expense)
      .leftMapIn(fromDBError)

  override def updateExpense(expenseId: ExpenseId, expense: Expense): F[Either[ApiError, Unit]] =
    expenseRepository
      .update(expenseId, expense)
      .leftMapIn(fromDBError)
      .map(_.map(_ => ()))

  override def deleteExpense(expenseId: ExpenseId): F[Either[ApiError, Unit]] =
    expenseRepository
      .delete(expenseId)
      .leftMapIn(fromDBError)
      .map(_.map(_ => ()))

  private def fromDBError(error: DBError): ApiError = error match {
    case UnexpectedDbError(message) =>
      ServerApiError(s"Unexpected database error: $message") // Если UnexpectedDbError уже является ApiError, просто возвращаем его. Если нет, необходимо создать соответствующий ApiError.
    case DBError.ConnectionDBError => ServerApiError("Problem with database connection")
    case DBError.NotFoundDbError   => UnauthorizedApiError("Resource not found")
  }

}
