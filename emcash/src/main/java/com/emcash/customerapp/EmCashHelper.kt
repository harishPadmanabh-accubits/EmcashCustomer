package com.emcash.customerapp

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.content.ContextCompat.startActivity
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.intro.IntroActivity

class EmCashHelper(val appContext: Context) {

    fun doEmCashLogin(
        phoneNUmber: String,
        password: String,
        fraction: String
    ) {
        val intent = Intent(appContext, IntroActivity::class.java).also {
            it.setFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)

    }

}