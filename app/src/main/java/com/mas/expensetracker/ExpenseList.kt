
import com.google.gson.annotations.SerializedName
import com.mas.expensetracker.Expense


class ExpenseList {

    val size: Int
        get() {
            return expenselist.size
        }
    val expenselist: ArrayList<Expense> = arrayListOf()

    fun add(toJson: Expense) {
        expenselist.add(toJson)
    }

    operator fun get(position: Int): Expense {
        return expenselist.get(position)
    }

}


