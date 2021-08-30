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
import kotlinx.android.synthetic.main.activity_main.*
import java.util.logging.Logger

class MainActivity : AppCompatActivity() ,EmCashListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        pb_login.show()
        EmCashHelper(applicationContext,this).doEmCashLogin(
            "509842776",
            "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
            "201812231321016084"
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