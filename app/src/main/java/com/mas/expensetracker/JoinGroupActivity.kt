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

class JoinGroupActivity: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var myGroups: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_join_group)
        database = Firebase.database
        databaseRef = database.reference
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        if (currentUser != null) {
            databaseRef.child("users").child(currentUser.uid).child("Groups").get().addOnSuccessListener {
                val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
                val arrayListType = object :
                    TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                myGroups = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
            }.addOnFailureListener {
                Log.e("firebase", "Error getting data1", it)
            }
        }
    }

    fun joinGroup(view: View) {
        var joinGroupIntent = Intent(this, JoinGroupActivity::class.java)
        finish()
        startActivity(joinGroupIntent)
        var key = findViewById<EditText>(R.id.join_group_et).text.toString()
        var participants = ArrayList<String>()
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser

        databaseRef.child("Groups").child(key).child("participants").get().addOnSuccessListener {
            val gson = Gson() //https://howtodoinjava.com/gson/gson-parse-json-array/
            val arrayListType = object :
                TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
            if (currentUser != null) {
                participants = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                participants.add(currentUser.email.toString())
                databaseRef.child("Groups").child(key).child("participants")
                    .setValue(gson.toJson(participants))
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data1", it)
        }
        if (currentUser != null) {
            myGroups.add(key)
            val gson = Gson()
            databaseRef.child("users").child(currentUser.uid).child("Groups").setValue(gson.toJson(myGroups))

            var expensesIntent = Intent(this, GroupsActivity::class.java)
            finish()
            startActivity(expensesIntent)
        }
    }
}