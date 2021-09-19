package com.emcash.customerapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.terms.TncStatus
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
            else -> handleLaunchNavigation(context)
        }

    }

    private fun handleLaunchNavigation(context: Context) :Intent{
        val syncManager =SyncManager(context)
        if (syncManager.shouldShowOnboardingScreens) {
            val intent = Intent(context, IntroActivity::class.java).also {
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
           return intent
        } else {
            if (syncManager.tncStatus == TncStatus.ACCEPTED) {
                val intent = Intent(context, HomeActivity::class.java).also {
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                return intent
            } else {
                val intent = Intent(context, IntroActivity::class.java).also {
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                return intent
            }

        }
    }

}