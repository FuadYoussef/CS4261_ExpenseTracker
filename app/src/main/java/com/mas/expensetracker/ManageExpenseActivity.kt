package com.mas.expensetracker

import ExpenseList
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ManageExpenseActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var pairs: ArrayList<Pair<String,Int>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_expense_view )
        database = Firebase.database
        databaseRef = database.reference

        val expense = getIntent().getSerializableExtra("expense")as Expense //Passed in Expense Object
        val textView : TextView = findViewById(R.id.managed_expense_name)
        textView.text= expense.expenseName

        var participantExpenses = expense.participantExpenses //List of (email,total) for each participant
        println(participantExpenses.get("test@mikunakano.com"))
//        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, participantExpenses)
//        val listView = findViewById<ListView>(R.id.participant_expense_list_view)
//        listView.setAdapter(arrayAdapter)
    }

    fun submitExpense(view: View) {
        database = Firebase.database
        databaseRef = database.reference
        val groupId = getIntent().getStringExtra("groupId")
        val expense = getIntent().getSerializableExtra("expense")as Expense //Passed in Expense Object
        val pos = Integer.parseInt(getIntent().getStringExtra("position").toString())
        println(pos)
        val textView : TextView = findViewById(R.id.managed_expense_name)
        textView.text= expense.expenseName

        var participantExpenses = expense.participantExpenses.toMutableMap() //List of (email,total) for each participant
        var participant = findViewById<EditText>(R.id.participant_et).text.toString()
        var value = Integer.parseInt(findViewById<EditText>(R.id.value_et).text.toString())
        if(participantExpenses.get(participant)!=null){
            participantExpenses.set(participant,value)
            expense.participantExpenses=participantExpenses
            println(expense.participantExpenses)
            if (groupId != null) {
                databaseRef.child("Groups").child(groupId).child("expenses").get().addOnSuccessListener {
                    val gson = Gson()
                    var objectList = gson.fromJson(it.value.toString(), ExpenseList::class.java)
                    objectList.set(pos,expense)
                    val expenseJson: String = gson.toJson(objectList)
                    println(expenseJson)
                    databaseRef.child("Groups").child(groupId).child("expenses").setValue(expenseJson)
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data1", it)
                }

            }
        }
        var groupViewIntent = Intent(this, GroupView::class.java)
        startActivity(groupViewIntent)
    }
}