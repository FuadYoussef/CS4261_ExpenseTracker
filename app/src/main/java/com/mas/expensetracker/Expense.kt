package com.mas.expensetracker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Expense(
     expenseName: String,
     expenseDescription: String,
     expenseTotal: Int,
     participantExpenses: Map<String, Int>
    ): Serializable {
    @SerializedName("expenseName")
    var expenseName: String = expenseName
    get() = field                     // getter
    set(value) {
        field = value
    }
    @SerializedName("expenseDescription")
    var expenseDescription: String = expenseDescription
        get() = field                     // getter
        set(value) {
            field = value
        }
    @SerializedName("expenseTotal")
    var expenseTotal: Int = expenseTotal
        get() = field                     // getter
        set(value) {
            field = value
        }
    @SerializedName("participantExpenses")
    var participantExpenses: Map<String, Int> = participantExpenses
        get() = field                     // getter
        set(value) {
            field = value
        }
}