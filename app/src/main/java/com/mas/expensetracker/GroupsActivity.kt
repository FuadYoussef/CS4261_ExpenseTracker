package com.mas.expensetracker

import android.content.Intent
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


class GroupsActivity: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var myGroups: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        database = Firebase.database
        databaseRef = database.reference
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        if (currentUser != null) {
            var userID = currentUser.uid
                databaseRef.child("users").child(currentUser.uid).child("Groups").get().addOnSuccessListener {
                    val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                    val arrayListType = object :
                        TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                    myGroups = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                    val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myGroups)
                    val listView = findViewById<ListView>(R.id.main_list_view)
                    listView.setAdapter(arrayAdapter)
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data1", it)
                }
            //can get more info about a group belonging to user by accessing databaseRef.child("Groups").child(GroupID).get()
            /*databaseRef.child("Groups").get().addOnSuccessListener {
                val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                val arrayListType = object : TypeToken<ArrayList<Group>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                userArray = gson.fromJson<ArrayList<Group>>(it.value.toString(), arrayListType)
                System.out.println("group list is: " + userArray)

            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }*/



        }

    }

    fun goToCreateScreen(view: View) {
        var createGroup = Intent(this, CreateGroupActivity::class.java)
        startActivity(createGroup)
    }
}