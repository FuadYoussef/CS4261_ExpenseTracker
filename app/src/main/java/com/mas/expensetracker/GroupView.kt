package com.mas.expensetracker

import ExpenseList
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GroupView : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var groupParticipants: ArrayList<String>
    private lateinit var expenseList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_view)
        database = Firebase.database
        databaseRef = database.reference
        val key =
            getIntent().getStringExtra("key").toString() //Passed in groupId from selected Group
        Log.i("GroupView", "Received groupId " + key)

        databaseRef.child("Groups").child(key).child("participants").get().addOnSuccessListener {
            val gson = Gson()
            val arrayListType = object :
                TypeToken<ArrayList<String>>() {}.type
            groupParticipants = gson.fromJson<ArrayList<String>>(
                it.value.toString(),
                arrayListType
            ) //ArrayList of Participants
            Log.i("GroupView", "Participants" + groupParticipants)
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groupParticipants)
            val listView = findViewById<ListView>(R.id.participant_list_view)
            listView.setAdapter(arrayAdapter)
        }.addOnFailureListener { e ->
            // Handle any errors
        }
        databaseRef.child("Groups").child(key).child("expenses").get().addOnSuccessListener {
            println(it.value.toString())
//        databaseRef.child("Groups").child(key).child("expenses").get().addOnSuccessListener {
//            if(it.value.toString()!=null){
//                println(it.value.toString())
//                val gson = GsonBuilder().setLenient().create()
//                val Model= gson.fromJson(it.value.toString(),Array<Expense>::class.java).toList()
//
//                for(expense in Model){
//                    println(expense)
//                }
//            }
//        }.addOnFailureListener { e ->
//            // Handle any errors
        }
    }

    fun goToGroupsScreen(view: View) {
        var groupsActivity = Intent(this, GroupsActivity::class.java)
        startActivity(groupsActivity)
    }

    fun goToCreateExpenseScreen(view: View) {
        database = Firebase.database
        databaseRef = database.reference
        val key = getIntent().getStringExtra("key").toString() //Passed in groupId from selected Group

        var createExpenseActivity = Intent(this, CreateExpenseActivity::class.java)
        createExpenseActivity.putExtra("key",key)
        createExpenseActivity.putStringArrayListExtra("participants",groupParticipants)
        startActivity(createExpenseActivity)
    }
}