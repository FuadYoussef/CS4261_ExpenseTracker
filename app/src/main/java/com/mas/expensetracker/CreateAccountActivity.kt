package com.mas.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class CreateAccountActivity: AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
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
            var userET = findViewById<EditText>(R.id.email_et)
            var username = userET.text.toString()
            if(username.isNotEmpty() && password.isNotEmpty()) {
                mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
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
            user.uid?.let { database.child("users").child(it).setValue(user.uid)}
            val gson = Gson()
            val arrayList =  ArrayList<String>()
            database.child("users").child(user.uid).child("Groups").setValue(gson.toJson(arrayList))

        }
    }

    fun navigateToLogin(view: View) {
        var loginIntent = Intent(this, MainActivity::class.java)
        startActivity(loginIntent)
    }
}