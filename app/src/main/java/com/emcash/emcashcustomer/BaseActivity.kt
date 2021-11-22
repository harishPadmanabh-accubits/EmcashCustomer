package com.emcash.emcashcustomer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emcash.customerapp.EmCashListener

class BaseActivity:AppCompatActivity(),EmCashListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}