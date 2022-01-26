package com.mas.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
            var expensesIntent = Intent(this, ExpensesActivity::class.java)
            startActivity(expensesIntent)
        }
    }
}