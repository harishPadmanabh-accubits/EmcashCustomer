package com.emcash.emcashcustomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emcash.customerapp.EmCashHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        EmCashHelper(applicationContext).doEmCashLogin(
            "509842776",
            "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31",
            "201812231321016084"


        )
      }
}