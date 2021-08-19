package com.emcash.customerapp

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.content.ContextCompat.startActivity
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountRequest
import com.emcash.customerapp.model.auth.userExists.UserExistCheckRequest
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.intro.IntroActivity
import com.emcash.customerapp.ui.prepare.PrepareEmCashActivity
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import com.emcash.customerapp.ui.terms.TncStatus
import com.emcash.customerapp.utils.LoaderDialog

class EmCashHelper(val appContext: Context,val listener:EmCashListener) {
    val syncManager = SyncManager(appContext)
    val authRepository = AuthRepository(appContext)

    fun doEmCashLogin(
        phoneNUmber: String,
        password: String,
        fraction: String
    ) {
        val request = SwitchAccountRequest(fraction, password, phoneNUmber)
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

    }


    private fun handleLaunchNavigation() {
        if (syncManager.shouldShowOnboardingScreens) {
            val intent = Intent(appContext, IntroActivity::class.java).also {
                it.setFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            appContext.startActivity(intent)
        } else {
            if (syncManager.tncStatus == TncStatus.ACCEPTED) {
                val intent = Intent(appContext, PrepareEmCashActivity::class.java).also {
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




}
public interface EmCashListener{
    fun onLoginSuccess(status:Boolean)
}