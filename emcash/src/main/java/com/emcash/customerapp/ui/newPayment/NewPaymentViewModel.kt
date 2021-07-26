package com.emcash.customerapp.ui.newPayment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*

class NewPaymentViewModel(val app: Application) : AndroidViewModel(app) {

    var _screen = MutableLiveData<NewPaymentScreens>().default(TRANSFER)
    val screens: LiveData<NewPaymentScreens> get() = _screen

    val validPin = "0000"

    fun gotoScreen(screen:NewPaymentScreens){
        _screen.value = screen
    }




}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT,PIN
}