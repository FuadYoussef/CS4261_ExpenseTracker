
import com.google.gson.annotations.SerializedName
import com.mas.expensetracker.Expense


class ExpenseList {

    val expenselist: ArrayList<Expense> = arrayListOf()

    fun add(toJson: Expense) {
        expenselist.add(toJson)
    }
}


