package com.emcash.emcashcustomer

import android.app.Application
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.BuildConfig
import com.emcash.customerapp.EmCashListener
import timber.log.Timber

class BaseApp:Application(),EmCashListener {
    var isCurrentlyOffline= MutableLiveData<Boolean>()

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

    }

}