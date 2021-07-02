package com.emcash.emcashcustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emcash.customerapp.ui.intro.IntroActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    fun onClick(view: View) {
        val intent=   Intent(applicationContext,IntroActivity::class.java)
        startActivity(intent)
    }
}