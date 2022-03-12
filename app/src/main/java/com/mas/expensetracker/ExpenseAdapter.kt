
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mas.expensetracker.Expense
import com.mas.expensetracker.GroupView
import com.mas.expensetracker.ManageExpenseActivity
import com.mas.expensetracker.R

class ExpenseAdapter(private val taskNames: ExpenseList) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTaskName: TextView = itemView.findViewById(R.id.expense_name)

        fun bind(taskName: Expense) {
            tvTaskName.text = taskName.expenseName
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.expense_list_name, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(taskNames[position])
        viewHolder.itemView.setOnClickListener { v->
            val expense = taskNames[position]
            val intent = Intent(v.context, ManageExpenseActivity::class.java)
            intent.putExtra("expense",expense)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return taskNames.size
    }

}