package com.mas.expensetracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GroupView : Activity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var groupParticipants: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_group_view)
            database = Firebase.database
            databaseRef = database.reference
            val key = getIntent().getStringExtra("element").toString() //Passed in groupId from selected Group
            Log.i("GroupView","Received groupId " + key)

            databaseRef.child("Groups").child(key).child("participants").get().addOnSuccessListener {
                Log.i("GroupView","Received groupId " + it.toString())
                val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                val arrayListType = object :
                    TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                groupParticipants = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groupParticipants)
                val listView = findViewById<ListView>(R.id.main_list_view)
                listView.setAdapter(arrayAdapter)
            }.addOnFailureListener { e ->
                // Handle any errors
            }
  }

    fun goToGroupsScreen(view: View) {
        var GroupsActivity = Intent(this, GroupsActivity::class.java)
        startActivity(GroupsActivity)
    }
}