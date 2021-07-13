package com.emcash.customerapp.ui.loademcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.extensions.default

class LoadEmCashViewModel(val app:Application):AndroidViewModel(app) {

    var _accountMode = MutableLiveData<AccountMode>().default(AccountMode.EMPAY)


}

enum class AccountMode{
    EMPAY,BANK_CARD
}