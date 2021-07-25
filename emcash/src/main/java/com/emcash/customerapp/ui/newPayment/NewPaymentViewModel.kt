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

    val maxPin = 4
    val validPin = "0000"
    var _currentPinPosition =MutableLiveData<Int>()
    val currentPinPosition :LiveData<Int> get() = _currentPinPosition

    var _pin=MutableLiveData<String>()
    val pin : LiveData<String> get() = _pin

    var isFromDelete = false
 //   var pin =""


    fun addToPin(digit:String){
        _pin.value = _pin.value.plus(digit)
    }

    fun removeLastFromPin(){
        _pin.value = _pin.value?.dropLast(1)
    }

}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT,PIN
}