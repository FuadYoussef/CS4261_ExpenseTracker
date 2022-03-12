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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GroupView : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var groupParticipants: ArrayList<String>
    private lateinit var rvexpenseList: RecyclerView

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
            val gson = Gson()

            rvexpenseList = findViewById(R.id.expense_list_view)
            rvexpenseList.hasFixedSize();
            rvexpenseList.layoutManager = LinearLayoutManager(this)
            var expenselist = gson.fromJson(it.value.toString(), ExpenseList::class.java)
            rvexpenseList.adapter = ExpenseAdapter(expenselist)

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