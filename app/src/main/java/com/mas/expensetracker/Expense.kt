package com.mas.expensetracker

class Expense(
     expenseName: String,
     expenseDescription: String,
     expenseTotal: Int,
     participantExpenses: Map<String, Int>
    ){
    var expenseName: String = expenseName
    get() = field                     // getter
    set(value) {
        field = value
    }
    var expenseDescription: String = expenseDescription
        get() = field                     // getter
        set(value) {
            field = value
        }
    var expenseTotal: Int = expenseTotal
        get() = field                     // getter
        set(value) {
            field = value
        }
    var participantExpenses: Map<String, Int> = participantExpenses
        get() = field                     // getter
        set(value) {
            field = value
        }
}