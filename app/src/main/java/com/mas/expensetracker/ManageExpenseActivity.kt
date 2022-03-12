package com.mas.expensetracker

import ExpenseAdapter
import ExpenseList
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        var participantExpenses = expense.participantExpenses.toList() //List of (email,total) for each participant

//        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, participantExpenses)
//        val listView = findViewById<ListView>(R.id.participant_expense_list_view)
//        listView.setAdapter(arrayAdapter)
    }
}