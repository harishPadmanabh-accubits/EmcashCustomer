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

        val button:Button= findViewById<Button>(R.id.button)
        button.setOnClickListener {
            try {
                pb_login.show()
                getFcmToken { status, token, error ->
                    when(status){
                        true->{
                            if (token != null) {
                                EmCashHelper(applicationContext,this).doEmCashLogin(
                                    "509842776",
                                    "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
                                    "201812231321016084",token
                                )
                            }
                        }false->{
                        Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
                    }
                    }
                }

            }catch (e:Exception){
                Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onLoginStatusCallback(status: Boolean) {
        if(status)
            pb_login.hide()
        else
            showShortToast("Login Failed.")
        pb_login.hide()


    }
    override fun onVerifyPin(forAction: TransactionType, sourceIfAny: Int) {
        startActivity(Intent(this,PinScreen::class.java).also {
            val typeBundle = bundleOf(KEY_TRANSACTION_TYPE to forAction)
            it.putExtra("KEY_TRANSACTION_TYPE",typeBundle)
            it.putExtra("SOURCE",sourceIfAny)
        })
    }

    override fun onEditProfile() {
        openEditProfile()
    }

    private fun openEditProfile() {
        showShortToast("Navigate to Empay Edit Profile Screen")
    }

    private fun getFcmToken(onTaskCompleted:(status:Boolean,token:String?,error:String?)->Unit){
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onTaskCompleted(false,null,task.exception?.message)
                    return@OnCompleteListener
                }
                val fcmToken = task.result.toString()
                onTaskCompleted(true,fcmToken,null)
            })

        }catch (e:Exception){
            e.printStackTrace()
            onTaskCompleted(false,null,e.message)
        }
    }

    override fun onGetFallBackScreen(): String {
        return className
    }
}