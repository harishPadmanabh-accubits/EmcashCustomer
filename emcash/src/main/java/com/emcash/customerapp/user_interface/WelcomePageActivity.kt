package com.emcash.customerapp.user_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity

class WelcomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

        Handler().postDelayed({
            openActivity(TermsAndConditionsActivity::class.java)
        }, 2000)
    }
}