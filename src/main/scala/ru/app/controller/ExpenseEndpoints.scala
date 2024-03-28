package ru.app.controller

import sttp.tapir._
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import ru.app.error.{ApiError, BusinessApiError, ServerApiError, UnauthorizedApiError}
import ru.app.model.{Expense, ExpenseId}
import sttp.model.StatusCode
import sttp.tapir.generic.auto.schemaForCaseClass

object ExpenseEndpoints {
  private val apiEndpoint: PublicEndpoint[Unit, ApiError, Unit, Any] =
    endpoint
      .errorOut(oneOf[ApiError](
        oneOfVariant(StatusCode.InternalServerError, jsonBody[ServerApiError]),
        oneOfVariant(StatusCode.UnprocessableEntity, jsonBody[BusinessApiError]),
        oneOfVariant(StatusCode.Unauthorized, jsonBody[UnauthorizedApiError])
      ))

  val listExpensesEndpoint: PublicEndpoint[Unit, ApiError, List[Expense], Any] =
    apiEndpoint
      .get
      .in("expenses")
      .out(jsonBody[List[Expense]])

  val readExpenseEndpoint: PublicEndpoint[ExpenseId, ApiError, Option[Expense], Any] =
    apiEndpoint
      .get
      .in("expenses" / path[ExpenseId]("id"))
      .out(jsonBody[Option[Expense]])

  val addExpenseEndpoint: PublicEndpoint[Expense, ApiError, ExpenseId, Any] =
    apiEndpoint
      .post
      .in("expenses")
      .in(jsonBody[Expense])
      .out(jsonBody[ExpenseId])

  val updateExpenseEndpoint: PublicEndpoint[(ExpenseId, Expense), ApiError, Unit, Any] =
    apiEndpoint
      .put
      .in("expenses" / path[ExpenseId]("id"))
      .in(jsonBody[Expense])

  val deleteExpenseEndpoint: Endpoint[Unit, ExpenseId, ApiError, Unit, Any] =
    apiEndpoint.delete
      .in("expenses" / path[ExpenseId]("id"))
}
