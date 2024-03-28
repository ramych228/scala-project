package ru.app.model

case class Expense(
  id: ExpenseId,
  userId: BigDecimal,
  amount: BigDecimal,
  description: Option[String]
)
