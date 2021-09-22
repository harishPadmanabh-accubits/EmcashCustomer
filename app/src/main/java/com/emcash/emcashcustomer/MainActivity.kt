package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashHelper
import com.emcash.customerapp.EmCashListener
import com.emcash.customerapp.enums.TransactionType
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.utils.IS_FROM_DEEPLINK
import com.emcash.customerapp.utils.KEY_DEEPLINK
import com.emcash.customerapp.utils.KEY_TRANSACTION_TYPE
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.Exception

class MainActivity : AppCompatActivity() ,EmCashListener{

    var token =""
    val isFromDeeplink by lazy {
        intent.getBooleanExtra(IS_FROM_DEEPLINK,false)
    }

    companion object{
        val className = "com.emcash.emcashcustomer.MainActivity"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        EmCashCommunicationHelper.setParentListener(this)
        if(isFromDeeplink){
            EmCashHelper(applicationContext,this).handleNotificationIntent(
                "509842776",
                "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
                "201812231321016084",intent.getStringExtra(KEY_DEEPLINK).toString()
            )
        }
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("fcmError", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val fcmToken = task.result.toString()
                token = fcmToken
                Log.d("fcmToken", fcmToken.toString())
            })

        } catch (exception: Exception) {
            Log.d("fcm exception", exception.toString())

        }


        val button:Button= findViewById(R.id.button) as Button
        button.setOnClickListener {
            Timber.e("HHP ENTERED LOGIN BUTTON")
            try {
                pb_login.show()
                EmCashHelper(applicationContext,this).doEmCashLogin(
                    "509842776",
                    "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
                    "201812231321016084",token
                )
            }catch (e:Exception){
                Timber.e("HHP ENTERED LOGIN BUTTON exc $e")
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
        }


    }


    override fun onLoginSuccess(status: Boolean) {
        Log.e("Listening","at Parent login status $status")
        if(status)
            pb_login.hide()
        else
            showShortToast("Login Failed")

    }
    override fun onVerifyPin(forAction: TransactionType, sourceIfAny: Int?) {
        startActivity(Intent(this,PinScreen::class.java).also {
            val typeBundle = bundleOf(KEY_TRANSACTION_TYPE to forAction)
            it.putExtra("KEY_TRANSACTION_TYPE",typeBundle)
            it.putExtra("SOURCE",sourceIfAny)
        })
    }
}