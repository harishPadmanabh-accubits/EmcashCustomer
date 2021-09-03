package com.emcash.customerapp.firebaseHelper

import android.content.Context
import com.emcash.customerapp.model.RemoteData
import timber.log.Timber

class PushHelper {
    companion object {
        private var instance: PushHelper? = null

        @JvmStatic
        fun getInstance(): PushHelper {
            return instance ?: synchronized(PushHelper::class.java) {
                val inst = instance ?: PushHelper()
                instance = inst
                inst
            }
        }
    }


    @Suppress("SENSELESS_COMPARISON")
    fun handlePushPayload(context: Context, remoteData: RemoteData) {
        try {
            if (context == null || remoteData == null) {
                Timber.e("Notification context null")
                return
            }
            Notifier.handleEmCashNotification(remoteData, context)
        } catch (e: Exception) {
            Timber.e(" handlePushPayload() : $e")
        }
    }
}

