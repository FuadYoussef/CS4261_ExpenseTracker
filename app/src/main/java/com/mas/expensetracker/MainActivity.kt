package com.mas.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        var currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    fun login(view: View) {
        var passwordET = findViewById<EditText>(R.id.password_et)
        var password = passwordET.text.toString()
        var userET = findViewById<EditText>(R.id.username_et)
        var username = userET.text.toString()
        /*Toast.makeText(
            this, "username is $username and password is $password",
            Toast.LENGTH_SHORT
        ).show();*/
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            System.out.println("made it to on complete listener")
            if (task.isSuccessful) {
                Toast.makeText(
                    this, "Authentication success.",
                    Toast.LENGTH_SHORT
                ).show();
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
    fun navigateToCreateAccount(view: View) {
        var createAccountIntent = Intent(this, CreateAccountActivity::class.java)
        startActivity(createAccountIntent)
    }

    fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            var expensesIntent = Intent(this, GroupsActivity::class.java)
            startActivity(expensesIntent)
        }
    }
}