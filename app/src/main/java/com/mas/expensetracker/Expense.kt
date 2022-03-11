package com.mas.expensetracker

class Expense(
    var expenseName: String,
    var expenseDescription: String,
    var expenseTotal: Int,
    var participantExpenses: Map<String, Int>
)