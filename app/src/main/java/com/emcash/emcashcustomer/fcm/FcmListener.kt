package com.emcash.emcashcustomer.fcm

import android.util.Log
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmListener : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM TOKEN", "$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("Fcm remote message", "$remoteMessage")
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val deepLink = remoteMessage.data["deepLink"]
        val type = remoteMessage.data["type"]
        val rejectContent = remoteMessage.data["rejectContent"]
        val emCashHelper =
            EmCashHelper(applicationContext, EmCashCommunicationHelper.getParentListener())
        emCashHelper.handleEmCashNotification(
            title,message,deepLink,type,rejectContent
        )

    }
}