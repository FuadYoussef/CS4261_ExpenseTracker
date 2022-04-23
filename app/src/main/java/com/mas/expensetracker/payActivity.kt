//Sampled from https://github.com/LucasPM97/GPaySample

package com.mas.expensetracker

import ExpenseList
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.PaymentData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mas.expensetracker.ui.completePay.CompletePayFragment
import kotlinx.android.synthetic.main.fragment_complete_pay.*
import org.json.JSONObject
import java.time.LocalDateTime

class payActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    lateinit var navController:NavController;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            // value passed in AutoResolveHelper
            CompletePayFragment.LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { intent ->
                            PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                        }
                    Activity.RESULT_CANCELED -> {
                        // Nothing to do here normally - the user simply cancelled without selecting a
                        // payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
                // Re-enables the Google Pay payment button.
                googlePayButton.isClickable = true
            }
        }
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        try {
            val paymentInformation = paymentData.toJson() ?: return
            val paymentMethodData = JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            Toast.makeText(this,"Thanks for Paying!", Toast.LENGTH_LONG).show()

            database = Firebase.database
            databaseRef = database.reference
            mAuth = FirebaseAuth.getInstance()
            val key = getIntent().getStringExtra("key").toString()
            val gson = Gson()
            var currentUser = mAuth.currentUser
            databaseRef.child("Groups").child(key).child("payments").get().addOnSuccessListener {
                if(it.value!=null) {
                    val arrayListType = object :
                        TypeToken<ArrayList<String>>() {}.type
                    var payments = gson.fromJson<ArrayList<String>>(it.value.toString(), arrayListType)
                    if (currentUser != null) {
                        payments.add(currentUser.email.toString())
                    }
                    val paymentJson: String = gson.toJson(payments)
                    databaseRef.child("Groups").child(key).child("payments").setValue(paymentJson)
                }else{
                    var payments = ArrayList<String>()
                    if (currentUser != null) {
                        payments.add(currentUser.email.toString())
                    }
                    val paymentJson: String = gson.toJson(payments)
                    databaseRef.child("Groups").child(key).child("payments").setValue(paymentJson)
                }
            }
            var groupViewIntent = Intent(this, GroupView::class.java)
            groupViewIntent.putExtra("key",key)
            startActivity(groupViewIntent)
        }
        catch (ex:Exception){
            print(ex)
        }
    }

    private fun handleError(statusCode: Int) {
        Toast.makeText(this,"Failed D:", Toast.LENGTH_LONG).show()
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

}
