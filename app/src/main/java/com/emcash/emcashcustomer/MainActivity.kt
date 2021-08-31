package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.emcash.customerapp.EmCashHelper
import com.emcash.customerapp.EmCashListener
import com.emcash.customerapp.TransactionType
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.utils.KEY_TRANSACTION_TYPE
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() ,EmCashListener{

    var token =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("fcmError", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val fcmToken = task.result.toString()
                token = fcmToken
                Log.d("fcmToken007", fcmToken.toString())
            })

        } catch (exception: Exception) {
            Log.d("fcm exception", exception.toString())

        }



    }

    fun onClick(view: View) {
        pb_login.show()
        EmCashHelper(applicationContext,this).doEmCashLogin(
            "509842776",
            "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
            "201812231321016084",token
        )
      }

    override fun onLoginSuccess(status: Boolean) {
        Log.e("Listening","at Parent login status $status")
        if(status)
            pb_login.hide()
        else
            showShortToast("Login Failed")

    }

    override fun onVerifyPin(forAction:TransactionType) {
        Log.e("On verify ","called listended in parent")
        startActivity(Intent(this,PinScreen::class.java).also {
            val typeBundle = bundleOf(KEY_TRANSACTION_TYPE to forAction)
            it.putExtra("KEY_TRANSACTION_TYPE",typeBundle)
        })
    }
}