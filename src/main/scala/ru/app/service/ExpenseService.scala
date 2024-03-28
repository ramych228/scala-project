package ru.app.service

import ru.app.error.ApiError
import ru.app.model.{Expense, ExpenseId}

trait ExpenseService[F[_]] {
  def listExpenses(): F[Either[ApiError, List[Expense]]]
  def addExpense(expense: Expense): F[Either[ApiError, ExpenseId]]
  def readExpense(expenseId: ExpenseId): F[Either[ApiError, Option[Expense]]]
  def updateExpense(expenseId: ExpenseId, expense: Expense): F[Either[ApiError, Unit]]
  def deleteExpense(expenseId: ExpenseId): F[Either[ApiError, Unit]]
}
