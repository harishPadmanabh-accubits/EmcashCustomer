package com.emcash.emcashcustomer.fcm

import android.util.Log
import android.widget.Toast
import com.emcash.customerapp.EmCashCommunicationHelper
import com.emcash.customerapp.EmCashHelper
import com.emcash.customerapp.firebaseHelper.EmCashFirebaseHelper
import com.emcash.customerapp.firebaseHelper.EmCashPushHelper
import com.emcash.customerapp.model.RemoteData
import com.emcash.emcashcustomer.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmListener : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCM TOKEN", "$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("Fcm remote message", "${remoteMessage.data}")
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val deepLink = remoteMessage.data["deepLink"]

        val remoteData = RemoteData(
            title,
            message,
            deepLink
        )
        EmCashFirebaseHelper.getInstance().passPushPayload(applicationContext,remoteData,MainActivity.className)


    }
}