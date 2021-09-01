package com.emcash.customerapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.utils.KEY_BEN_ID
import com.emcash.customerapp.utils.KEY_USER_ID_FROM_DEEPLINK
import com.emcash.customerapp.utils.LAUNCH_DESTINATION
import com.emcash.customerapp.utils.SCREEN_CHAT
import timber.log.Timber

object DeepLinkFactory {
    fun getIntentFromDeeplink(url: Uri, context: Context): Intent {
        Timber.e("Path segments ${url.pathSegments[0]}  ${url.pathSegments[1]}")
        return when(url.pathSegments[1]){
            "paymentHistory"->{
                val userId = url.pathSegments[2]
                Timber.e("User id from depplink path $userId")
                Intent(context,NewPaymentActivity::class.java).also {
                    it.putExtra(LAUNCH_DESTINATION, SCREEN_CHAT)
                    it.putExtra(KEY_USER_ID_FROM_DEEPLINK,userId)
                    it.putExtra(KEY_BEN_ID,userId.toInt())
                }


            }
            else -> throw IllegalArgumentException("Invalid Deeplink ")
        }

    }
}