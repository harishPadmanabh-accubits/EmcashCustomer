package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun onClick(view: View) {
        val intent=   Intent(applicationContext,PrepareEmCashActivity::class.java)
        startActivity(intent)
    }
}