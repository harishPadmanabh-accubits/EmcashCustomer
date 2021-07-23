package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import timber.log.Timber

class NewPaymentActivity : AppCompatActivity() {

    val viewModel:NewPaymentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_payment)


   
    }
}