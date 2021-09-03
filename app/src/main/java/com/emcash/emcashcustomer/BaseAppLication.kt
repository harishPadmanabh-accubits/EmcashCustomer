package com.emcash.emcashcustomer

import android.app.Application
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashListener

class BaseApplication:Application(),EmCashListener {
    override fun onCreate() {
        super.onCreate()
        EmCashCommunicationHelper.setParentListener(this)

    }
}