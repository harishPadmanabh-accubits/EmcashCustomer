package com.emcash.emcashcustomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.emcash.customerapp.EmCashHelper
import com.emcash.customerapp.EmCashListener
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
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
}