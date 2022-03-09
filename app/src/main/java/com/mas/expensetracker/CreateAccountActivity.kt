package com.mas.expensetracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreateAccountActivity: AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var existingUsernames: ArrayList<String>
    var newUsername = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        mAuth = FirebaseAuth.getInstance()
    }


    fun createAccount(view: View) {
        var passwordET = findViewById<EditText>(R.id.password_et)
        var vPasswordET = findViewById<EditText>(R.id.verify_password_et)
        if (passwordET.text.toString().equals(vPasswordET.text.toString())){
            var password = passwordET.text.toString()
            var emailET = findViewById<EditText>(R.id.email_et)
            var usernameET = findViewById<EditText>(R.id.username_et)
            newUsername = usernameET.text.toString()
            var userEmail = emailET.text.toString()
            if(userEmail.isNotEmpty() && password.isNotEmpty() && newUsername.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }

            }
        }
    }

    fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            var expensesIntent = Intent(this, GroupsActivity::class.java)
            startActivity(expensesIntent)
            var database = Firebase.database.reference
            val gson = Gson()
            val arrayList =  ArrayList<String>()

            /*database.child("existingUsernames").get().addOnSuccessListener {
                val arrayListType = object :
                    TypeToken<ArrayList<String>>() {}.type //https://www.bezkoder.com/kotlin-parse-json-gson/
                existingUsernames = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
            }.addOnFailureListener {
                Log.e("firebase", "Error getting existing usernames", it)
            }*/
            //if(!existingUsernames.contains(newUsername)) {
                user.uid?.let { database.child("users").child(newUsername).setValue(newUsername)}
                user.uid?.let { database.child("uidToUsername").child(it).child("username").setValue(newUsername)}
                //existingUsernames.add(newUsername)
                //database.child("existingUsernames").setValue(gson.toJson(existingUsernames))
            //}


            database.child("users").child(newUsername).child("Groups").setValue(gson.toJson(arrayList))

        }
    }
}