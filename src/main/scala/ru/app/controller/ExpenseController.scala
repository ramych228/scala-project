package ru.app.controller

import ru.app.controller.ExpenseEndpoints._
import cats.effect.Sync
import ru.app.service.ExpenseService
import sttp.tapir.server.ServerEndpoint

final class ExpenseController[F[_]: Sync](
  expenseService: ExpenseService[F]
) {
  private val listExpensesServerEndpoint: ServerEndpoint[Any, F] =
    listExpensesEndpoint
      .serverLogic { _ =>
        expenseService
          .listExpenses()
      }

  private val readExpenseServerEndpoint: ServerEndpoint[Any, F] =
    readExpenseEndpoint
      .serverLogic { expenseId =>
        expenseService.readExpense(expenseId)
      }


  private val addExpenseServerEndpoint: ServerEndpoint[Any, F] =
    addExpenseEndpoint
      .serverLogic(newExpense => expenseService.addExpense(newExpense))

  private val updateExpenseServerEndpoint: ServerEndpoint[Any, F] =
    updateExpenseEndpoint
      .serverLogic { case (expenseId, expense) =>
        expenseService.updateExpense(expenseId, expense)
      }

  private val deleteExpenseServerEndpoint: ServerEndpoint[Any, F] =
    deleteExpenseEndpoint
      .serverLogic { expenseId =>
        expenseService
          .deleteExpense(expenseId)
      }

  val apiEndpoints: List[ServerEndpoint[Any, F]] =
    List(
      readExpenseServerEndpoint,
      listExpensesServerEndpoint,
      addExpenseServerEndpoint,
      updateExpenseServerEndpoint,
      deleteExpenseServerEndpoint
    )
}
