package com.mas.expensetracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ExpensesActivity: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        database = Firebase.database
        databaseRef = database.reference
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        if (currentUser != null) {

            var userID = currentUser.uid
            databaseRef.child("users").child(userID).child("listJSON").get().addOnSuccessListener {
                val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                val arrayListType = object : TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                userArray = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                val arrayAdapter: ArrayAdapter<String> =
                    ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userArray)
                val listView = findViewById<ListView>(R.id.main_list_view)
                listView.setAdapter(arrayAdapter)
                val expenseCount = findViewById<TextView>(R.id.expense_value_label)
                expenseCount.text = userArray.size.toString()

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }


        }

    }

    fun addExpense(view: View) {
        val newExpenseET = findViewById<EditText>(R.id.new_expense_et)
        userArray.add(newExpenseET.text.toString())
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        if (currentUser != null) {

            var userID = currentUser.uid
            val gson = Gson()
            databaseRef.child("users").child(userID).child("listJSON").setValue(gson.toJson(userArray))

        }
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userArray)
        val listView = findViewById<ListView>(R.id.main_list_view)
        listView.setAdapter(arrayAdapter)
        val expenseCount = findViewById<TextView>(R.id.expense_value_label)
        expenseCount.text = userArray.size.toString()
    }
}