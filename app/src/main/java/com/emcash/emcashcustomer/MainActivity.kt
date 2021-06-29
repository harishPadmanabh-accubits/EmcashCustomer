package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.user_interface.IntroActivity
import com.emcash.customerapp.user_interface.WelcomePageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent=   Intent(applicationContext,WelcomePageActivity::class.java)
        startActivity(intent)
    }
}