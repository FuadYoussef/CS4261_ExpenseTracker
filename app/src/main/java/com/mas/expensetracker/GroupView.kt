package com.mas.expensetracker

import ExpenseAdapter
import ExpenseList
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.security.Key


class GroupView : AppCompatActivity(), CustomDialog.OnInputListener {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var groupParticipants: ArrayList<String>
    private lateinit var rvexpenseList: RecyclerView
    private lateinit var classKey: String
    private lateinit var listView: ListView
    private var screenSizeInt = 1
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_view)
        database = Firebase.database
        databaseRef = database.reference
        var clubNameTV = findViewById<TextView>(R.id.text_members)
        var mAuth = FirebaseAuth.getInstance()
        var currentUser = mAuth.currentUser
        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK


        screenSizeInt = when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> 0
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> 1
            Configuration.SCREENLAYOUT_SIZE_SMALL -> 2
            else -> 3
        }
        key =
            getIntent().getStringExtra("key").toString() //Passed in groupId from selected Group
        classKey = key
        val myGroups = getIntent().getStringArrayListExtra("myGroups")

        val textView : TextView = findViewById(R.id.text_owner)
        databaseRef.child("Groups").child(key).child("groupCreatorEmail").get().addOnSuccessListener{
            val owner = it.value.toString()
            textView.text= "Group Owner: " + owner
        }

//        if(screenSizeInt == 1 || screenSizeInt == 2) {
//            val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
//            fab.visibility = View.INVISIBLE
//        }

        databaseRef.child("Groups").child(key).child("groupName").get().addOnSuccessListener {
            clubNameTV.text = it.value.toString()
        }

        databaseRef.child("Groups").child(key).child("participants").get().addOnSuccessListener {
            val gson = Gson()
            val arrayListType = object :
                TypeToken<ArrayList<String>>() {}.type
            groupParticipants = gson.fromJson<ArrayList<String>>(
                it.value.toString(),
                arrayListType
            ) //ArrayList of Participants
            if (currentUser != null && !groupParticipants.contains(currentUser.email) && myGroups != null) {
                myGroups.remove(key)
                databaseRef.child("users").child(currentUser.uid).child("Groups").setValue(myGroups)
                var groupsActivity = Intent(this, GroupsActivity::class.java)
                this.finish()
                startActivity(groupsActivity)

            }
            Log.i("GroupView", "Participants" + groupParticipants)
            arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groupParticipants)
            listView = findViewById<ListView>(R.id.recyclerView2)
            listView.setAdapter(arrayAdapter)
            if (screenSizeInt == 0 || screenSizeInt == 3) {
                listView.onItemClickListener =
                    OnItemClickListener { parent, view, position, id ->
                        groupParticipants.removeAt(position)
                        databaseRef.child("Groups").child(key).child("participants").setValue(groupParticipants)
                        arrayAdapter.notifyDataSetChanged()
                    }
            }
        }.addOnFailureListener { e ->
            // Handle any errors
        }
        databaseRef.child("Groups").child(key).child("expenses").get().addOnSuccessListener {
            val gson = Gson()

            rvexpenseList = findViewById(R.id.expense_list_view)
            rvexpenseList.hasFixedSize();
            rvexpenseList.layoutManager = LinearLayoutManager(this)
            val groupId = getIntent().getStringExtra("key").toString()
            var expenselist = gson.fromJson(it.value.toString(), ExpenseList::class.java)
            if(expenselist!= null){
                rvexpenseList.adapter = ExpenseAdapter(expenselist,groupId)
            }
        }
    }

    fun goToGroupsScreen(view: View) {
        var groupsActivity = Intent(this, GroupsActivity::class.java)
        startActivity(groupsActivity)
    }

    fun goToPayScreen(view: View) {
        val key = getIntent().getStringExtra("key").toString()
        var payActivity = Intent(this, payActivity::class.java)
        payActivity.putExtra("key",key)
        startActivity(payActivity)
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

    override fun manageGroup() {
        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                groupParticipants.removeAt(position)
                databaseRef.child("Groups").child(key).child("participants").setValue(groupParticipants)
                arrayAdapter.notifyDataSetChanged()
            }
    }

    override fun printAudit() {
        var root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        root = File(root, "ExpenseTracker")
        root.mkdir()
        root = File(root, "audit.txt")

        try {
            var fout = FileOutputStream(root);
            val osw = OutputStreamWriter(fout)
            try {
                databaseRef.child("Groups").child(classKey).child("expenses").get().addOnSuccessListener {
                    val gson = Gson()
                    val groupId = getIntent().getStringExtra("key").toString()
                    var expenselist = gson.fromJson(it.value.toString(), ExpenseList::class.java)
                    for (e in  expenselist.expenselist) {
                        osw.write("**********************")
                        osw.write("\n")
                        osw.write("Expense Name: " + e.expenseName)
                        osw.write("\n")
                        osw.write("Expense Description: " + e.expenseDescription)
                        osw.write("\n")
                        osw.write("Expense Total: " + e.expenseTotal)
                        osw.write("\n")
                        osw.write("Participant Details:\n")
                        for (p in e.participantExpenses.entries.iterator()) {
                            osw.write("-------------------\n")
                            osw.write("${p.key} owes ${p.value}\n")
                            /*if (p.hasPayed) {
                                osw.write("${p} has paid\n")
                            } else {
                                osw.write("${p} has not paid\n")
                            }*/
                        }
                        osw.write("**********************")
                    }
                    //osw.write(it.toString())
                    osw.flush()
                    osw.close()
                    fout.close();
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace();

            var bool = false
            try {
                bool = root.createNewFile()
            } catch (e1: IOException) {
                e1.printStackTrace();
            }
            if (bool){
                // call the method again
                printAudit()
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun manageAccount(view: View) {
        if (screenSizeInt == 0 || screenSizeInt == 3) {
            val dialog = CustomDialog()
            dialog.show(supportFragmentManager, "CustomDialog")
        }
    }


}