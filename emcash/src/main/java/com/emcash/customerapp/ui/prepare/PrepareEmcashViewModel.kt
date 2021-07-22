package com.emcash.customerapp.ui.prepare

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.extensions.default

class PrepareEmcashViewModel(val app : Application):AndroidViewModel(app) {

    var _bottomSheetVisiblity = MutableLiveData<Boolean>()
    val bottomSheetVisibility : LiveData<Boolean> get() = _bottomSheetVisiblity

}