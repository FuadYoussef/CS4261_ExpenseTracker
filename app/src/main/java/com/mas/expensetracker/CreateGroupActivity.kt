package com.mas.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateGroupActivity: AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        database = Firebase.database
        databaseRef = database.reference
    }

    fun createGroup(view: View) {
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        val participantsET = findViewById<EditText>(R.id.participants_et)
        var participants = participantsET.text.toString().split(',') as ArrayList<String>
        if (currentUser != null) {
            val groupName = findViewById<EditText>(R.id.group_name_et).text.toString()
            participants.add(currentUser.email.toString())
            val groupDescription = findViewById<EditText>(R.id.group_description_et).text.toString()
            val key = database.getReference("Groups").push().key
            databaseRef.child("Groups").child(key.toString()).child("participants").setValue(participants)
            databaseRef.child("Groups").child(key.toString()).child("groupName").setValue(groupName)
            databaseRef.child("Groups").child(key.toString()).child("groupDescription").setValue(groupDescription)
            var expensesIntent = Intent(this, GroupsActivity::class.java)
            finish()
            startActivity(expensesIntent)
        }
    }

}