package com.emcash.customerapp

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.net.toUri
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.data.repos.PaymentRepository
import com.emcash.customerapp.enums.TransactionType
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
        EmCashCommunicationHelper.apply {
            setParentListener(listener)
            setFallbackTo(getFallBackScreen())
        }

    }

    private fun getFallBackScreen() = listener.onGetFallBackScreen()
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
        syncManager.fcmToken = token
        authRepository.performSwitchAccount(
            request,
            onApiCallBack = { status, _, error ->
                when (status) {
                    true -> {
                        listener.onLoginStatusCallback(true)
                        handleLaunchNavigation()
                    }
                    false -> {
                        listener.onLoginStatusCallback(false)
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
    fun onPinVerified(forAction: TransactionType,launchSource:Int) {
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
    
    fun onPinInputCancelled(){
        val intent = Intent(appContext, NewPaymentActivity::class.java).also {
            it.setFlags(FLAG_ACTIVITY_NEW_TASK)
            it.putExtra(LAUNCH_SOURCE, PIN_CANCEL)
            it.putExtra(LAUNCH_DESTINATION, SCREEN_TRANSFER)
            it.putExtra(KEY_IS_FROM_CANCEL_PIN,true)
        }

        appContext.startActivity(intent)
    }

    fun proceedToTransfer() {
        PaymentRepository(appContext).transferAmount { status, error ->
            when (status) {
                true -> {
                    syncManager.transferScreenCache=null
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
                    appContext.showShortToast(appContext.getString(R.string.error_transfer))
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

    fun handleNotificationIntent(
        phoneNumber: String,
        password: String,
        fraction: String,
        deeplink:String
    ){
        val token = syncManager.fcmToken
        val request = SwitchAccountRequest(fraction, password, phoneNumber,token)
        Timber.e("Request login $request")
        authRepository.performSwitchAccount(
            request,
            onApiCallBack = { status, response, error ->
                when (status) {
                    true -> {
                        listener.onLoginStatusCallback(true)
                        handleDeepLink(deeplink)
                    }
                    false -> {
                        listener.onLoginStatusCallback(false)
                        appContext.showShortToast(error)
                    }
                }
            })

    }

    private fun handleDeepLink(deeplink: String) {
       val notifyIntent =   DeepLinkFactory.getIntentFromDeeplink(deeplink.toUri(),appContext).also {
           it.setFlags(FLAG_ACTIVITY_NEW_TASK)
       }
       appContext.startActivity(notifyIntent)
    }


}

interface EmCashListener {
    fun onLoginStatusCallback(status: Boolean) {}
    fun onVerifyPin(forAction: TransactionType, sourceIfAny:Int = 0) {}
    fun onEditProfile(){}
    fun onGetFallBackScreen():String{
        return ""
    }
}

