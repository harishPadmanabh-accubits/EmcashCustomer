package com.emcash.customerapp.ui.convert_emcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawRequest

class WithdrawViewModel(val app : Application):AndroidViewModel(app) {
    val homeRepository = HomeRepository(app)

    fun withdraw(
        amount:Int,
        iban:String,
        onFinished: (status:Boolean,error:String?)->Unit
    ){

      homeRepository.withdrawFromWallet(WalletWithdrawRequest(amount,iban),onApiCallBack = {
          status, response, error ->
          when(status){
              true-> onFinished(true,null)
              false->onFinished(false,error)
          }
      })
    }

}