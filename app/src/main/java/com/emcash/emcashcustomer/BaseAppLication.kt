package com.emcash.emcashcustomer

import android.app.Application
import com.emcash.customerapp.BuildConfig
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashListener
import timber.log.Timber

class BaseApp:Application(),EmCashListener {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

    }
}