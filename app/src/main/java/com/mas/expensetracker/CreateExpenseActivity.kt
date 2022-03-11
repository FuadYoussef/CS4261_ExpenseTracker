package com.mas.expensetracker

import ExpenseList
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class CreateExpenseActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var listOfParticipantExpenses: Map<String,Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_expense)
        database = Firebase.database
        databaseRef = database.reference

    }

    fun createExpense(view: View){
        database = Firebase.database
        databaseRef = database.reference
        val participants = getIntent().getStringArrayListExtra("participants") //Passed in participant arraylist
        val key = getIntent().getStringExtra("key").toString() //Passed in groupId
        Log.i("CreateExpense","Passed in Groupid " + key)
        Log.i("CreateExpense","Passed in Participants " + participants)

        if(participants!=null){
            listOfParticipantExpenses = participants.associateWith { 0 }
            Log.i("CreateExpense","Created listOfParticipantExpenses " + listOfParticipantExpenses)


            val expenseName = findViewById<EditText>(R.id.expense_name_et).text.toString()
            val expenseDescription = findViewById<EditText>(R.id.expense_description_et).text.toString()
            val expenseTotal = findViewById<EditText>(R.id.expense_total_et).text.toString().toInt()
            val expense = Expense(expenseName,expenseDescription,expenseTotal,listOfParticipantExpenses)

            databaseRef.child("Groups").child(key).child("expenses").get().addOnSuccessListener {
                val gson = Gson()
                val arrayListType = object :TypeToken<ArrayList<Expense>>() {}.type

                if(it.value!=null){
                    Log.i("CreateExpense","Pulled" + it.value)
                    var objectList = gson.fromJson(it.value.toString(), ExpenseList::class.java)
                    Log.i("CreateExpense","Pulled" + objectList)
                    objectList.add(expense)
                    val expenseJson: String = gson.toJson(objectList)
                    Log.i("CreateExpense","All Expenses" + expenseJson)
                    databaseRef.child("Groups").child(key).child("expenses").setValue(expenseJson)
                }else{
                    val expenseJson: String = gson.toJson(expense)
                    databaseRef.child("Groups").child(key).child("expenses").setValue(expenseJson)
                }

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data1", it)
            }
//            val gson = Gson()
//            val expenseJson: String = gson.toJson(expense)
//            databaseRef.child("Groups").child(key).child("expenses").push().setValue(expenseJson)


            var groupViewIntent = Intent(this, GroupView::class.java)
            groupViewIntent.putExtra("key",key)
            finish()
            startActivity(groupViewIntent)

        }

    }


}