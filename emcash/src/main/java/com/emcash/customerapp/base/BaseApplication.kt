package com.emcash.customerapp.base

import android.app.Application
import com.emcash.customerapp.BuildConfig
import timber.log.Timber

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}