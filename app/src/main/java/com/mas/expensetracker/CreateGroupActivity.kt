package com.mas.expensetracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateGroupActivity: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var myGroups: ArrayList<String>
    private lateinit var participantGroups: ArrayList<String>
    var currentUserUsername = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        database = Firebase.database
        databaseRef = database.reference
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        if (currentUser != null) {
            databaseRef.child("uidToUsername").child(currentUser.uid).child("username").get()
                .addOnSuccessListener { uName ->
                    currentUserUsername = uName.value.toString()
                    databaseRef.child("users").child(currentUserUsername).child("Groups").get()
                        .addOnSuccessListener {
                            val gson =
                                Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                            val arrayListType = object :
                                TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                            myGroups =
                                gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                        }.addOnFailureListener {
                        Log.e("firebase", "Error getting data1", it)
                    }
                }
        }
    }

    fun createGroup(view: View) {
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        val participantsET = findViewById<EditText>(R.id.participants_et)
        var participants = participantsET.text.toString().split(',') as ArrayList<String>
        if (currentUser != null) {
            databaseRef.child("uidToUsername").child(currentUser.uid).child("username").get()
                .addOnSuccessListener { uName ->
                    currentUserUsername = uName.value.toString()
                    System.out.println("made it into this loop")
                    val groupName = findViewById<EditText>(R.id.group_name_et).text.toString()
                    participants.add(currentUserUsername)
                    val groupDescription =
                        findViewById<EditText>(R.id.group_description_et).text.toString()
                    val key = database.getReference("Groups").push().key
                    databaseRef.child("Groups").child(key.toString()).child("participants")
                        .setValue(participants)
                    databaseRef.child("Groups").child(key.toString()).child("groupName")
                        .setValue(groupName)
                    databaseRef.child("Groups").child(key.toString()).child("groupDescription")
                        .setValue(groupDescription)
                    val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                    /*for(p in participants) {
                databaseRef.child("users").child(p).child("Groups").get().addOnSuccessListener {
                    val arrayListType = object :
                        TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                    participantGroups = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                    participantGroups.add(key.toString())
                   // databaseRef.child("users").child(p).child("Groups").setValue(gson.toJson(participantGroups))
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data1", it)
                }
            }*/

                    myGroups.add(key.toString())
                    databaseRef.child("users").child(currentUserUsername).child("Groups")
                        .setValue(gson.toJson(myGroups))
                }
            var expensesIntent = Intent(this, GroupsActivity::class.java)
            finish()
            startActivity(expensesIntent)
        }
    }

}