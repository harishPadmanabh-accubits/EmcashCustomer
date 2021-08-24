package com.emcash.customerapp.ui.loademcash

import android.app.Application
import android.content.IntentSender
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import timber.log.Timber

class LoadEmCashViewModel(val app:Application):AndroidViewModel(app) {

    val homeRepository = HomeRepository(app)

    var _accountMode = MutableLiveData<AccountMode>().default(AccountMode.EMPAY)

    fun addEmCash(
        amount:Int,
        desc:String,
        onFinished: (status:Boolean,error:String?)->Unit
    ){
        val topupRequest = WalletTopupRequest(amount,desc)
        homeRepository.topupWallet(topupRequest){
                status, response, error ->
            onFinished(status,error)

        }
    }

}

enum class AccountMode{
    EMPAY,BANK_CARD
}