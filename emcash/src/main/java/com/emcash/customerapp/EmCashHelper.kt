package com.emcash.customerapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.os.Build
import androidx.core.app.TaskStackBuilder
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.data.repos.PaymentRepository
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountRequest
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.terms.TncStatus
import com.emcash.customerapp.utils.*
import timber.log.Timber

class EmCashHelper(val appContext: Context, val listener: EmCashListener) {
    val syncManager = SyncManager(appContext)
    val authRepository = AuthRepository(appContext)

    init {
        EmCashCommunicationHelper.setParentListener(listener)
    }

    fun doEmCashLogin(
        phoneNumber: String,
        password: String,
        fraction: String,
        token: String
    ) {
//        if(syncManager.sessionId!=null){
//            handleLaunchNavigation()
//        }else{
        val request = SwitchAccountRequest(fraction, password, phoneNumber,token)
        authRepository.performSwitchAccount(
            request,
            onApiCallBack = { status, response, error ->
                when (status) {
                    true -> {
                        listener.onLoginSuccess(true)
                        handleLaunchNavigation()
                    }
                    false -> {
                        listener.onLoginSuccess(false)
                        appContext.showShortToast(error)
                    }
                }
            })
        //  }


    }


    private fun handleLaunchNavigation() {
        if (syncManager.shouldShowOnboardingScreens) {
            val intent = Intent(appContext, IntroActivity::class.java).also {
                it.setFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        } else {
            if (syncManager.tncStatus == TncStatus.ACCEPTED) {
                val intent = Intent(appContext, HomeActivity::class.java).also {
                    it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                }
                appContext.startActivity(intent)
            } else {
                val intent = Intent(appContext, IntroActivity::class.java).also {
                    it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                }
                appContext.startActivity(intent)
            }

        }
    }


    fun onPinVerified(forAction: TransactionType) {
        Timber.e("Type Recieved $forAction")
        when (forAction) {
            TransactionType.TRANSFER -> proceedToTransfer()
            TransactionType.REQUEST -> {
            }
            TransactionType.ACCEPT -> proceedToAcceptPayment()
            TransactionType.REJECT -> proceedToRejectPayment()
            TransactionType.VERIFY_USER->{

            }

        }
    }

    fun proceedToTransfer() {
        PaymentRepository(appContext).transferAmount { status, error ->
            when (status) {
                true -> {
                    val intent = Intent(appContext, NewPaymentActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    intent.putExtra(LAUNCH_SOURCE, SCREEN_RECEIPT)
                    intent.putExtra(LAUNCH_DESTINATION, SCREEN_RECEIPT)
                    appContext.startActivity(intent)
                }
                false -> {
                    val intent = Intent(appContext, HomeActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    appContext.showShortToast("Transfer Failed.Try again after some time.")
                    appContext.startActivity(intent)
                }
            }
        }
    }

    fun proceedToAcceptPayment() {
        PaymentRepository(appContext).acceptPayment { status, error ->
            when (status) {
                true -> {
                    val intent = Intent(appContext, NewPaymentActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    intent.putExtra(LAUNCH_SOURCE, SCREEN_RECEIPT)
                    intent.putExtra(LAUNCH_DESTINATION, SCREEN_RECEIPT)
                    appContext.startActivity(intent)
                }
                false -> {
                    val intent = Intent(appContext, HomeActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    appContext.showShortToast("Transfer Failed.Try again after some time.")
                    appContext.startActivity(intent)
                }
            }
        }

    }

    fun proceedToRejectPayment() {
        PaymentRepository(appContext).rejectPayment { status, error ->
            when (status) {
                true -> {
                    val intent = Intent(appContext, NewPaymentActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    intent.putExtra(LAUNCH_SOURCE, SCREEN_RECEIPT)
                    intent.putExtra(LAUNCH_DESTINATION, SCREEN_RECEIPT)
                    appContext.startActivity(intent)
                }
                false -> {
                    val intent = Intent(appContext, HomeActivity::class.java).also {
                        it.setFlags(FLAG_ACTIVITY_NEW_TASK)
                    }
                    appContext.showShortToast("Transfer Failed.Try again after some time.")
                    appContext.startActivity(intent)
                }
            }
        }


    }

    fun handleEmCashNotification(
        title: String?,
        message: String?,
        deepLink: String?,
        type: String?,
        rejectContent: String?
    ) {
        Timber.e("fcm $title,$message,$deepLink")
        if (title != null && message != null) {
            showNotification(title,message,deepLink)
        }else{
            Timber.e("Arrived null on notify")
        }

    }

    private fun showNotification(title: String?, message: String?, deeplink: String?) {
        val channelId = "com.emcash.customer.notifications"
        val channeldesc = "Emcash customer Notification Channel"
        val builder: Notification.Builder

        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(appContext,HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(KEY_DEEPLINK, deeplink?.toString())
            putExtra(KEY_TYPE, type?.toString())
            putExtra(IS_FROM_DEEPLINK, true)
        }

        val stackBuilder = TaskStackBuilder.create(appContext).also {
            it.addParentStack(HomeActivity::class.java)
            it.addNextIntent(notificationIntent)
        }
        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channeldesc,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(appContext, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.intro_small_coin)
                .setBadgeIconType(R.mipmap.ic_launcher)
        } else {

            builder = Notification.Builder(appContext)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)

        }


        notificationManager.notify(10001, builder.build())

    }


}

interface EmCashListener {
    fun onLoginSuccess(status: Boolean) {}
    fun onVerifyPin(forAction: TransactionType,sourceIfAny:Int?=null) {}
}

enum class TransactionType {
    TRANSFER, REQUEST, ACCEPT, REJECT, VERIFY_USER
}