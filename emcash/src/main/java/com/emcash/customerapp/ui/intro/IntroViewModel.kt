package com.emcash.customerapp.ui.intro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.extensions.default

class IntroViewModel(val app: Application) : AndroidViewModel(app) {

    var _screenPosition = MutableLiveData<Int>().default(0)
    val screenPosition: LiveData<Int>
        get() = _screenPosition


}