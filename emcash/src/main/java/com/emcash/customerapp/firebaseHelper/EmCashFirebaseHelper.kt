package com.emcash.customerapp.firebaseHelper

import android.content.Context
import com.emcash.customerapp.model.RemoteData
import timber.log.Timber
import java.lang.Exception

class EmCashFirebaseHelper {
    companion object {
        private var instance: EmCashFirebaseHelper? = null

        @JvmStatic
        fun getInstance(): EmCashFirebaseHelper {
            if (instance == null) {
                synchronized(EmCashFirebaseHelper::class.java) {
                    if (instance == null) instance = EmCashFirebaseHelper()
                }
            }

            return instance as EmCashFirebaseHelper
        }
    }

    fun passPushPayload(context: Context?, remoteData:RemoteData?,launchClassName:String){
        try {
            if (context == null || remoteData == null) return
            PushHelper.getInstance().handlePushPayload(context, remoteData,launchClassName)
        }catch (e:Exception){
            Timber.e("Exception in passPushPayload $e")
            e.printStackTrace()
        }

    }
}