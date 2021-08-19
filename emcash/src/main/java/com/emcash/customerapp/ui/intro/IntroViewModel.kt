package com.emcash.customerapp.ui.intro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.extensions.default
import kotlinx.coroutines.async

class IntroViewModel(val app: Application) : AndroidViewModel(app) {

    var _screenPosition = MutableLiveData<Int>().default(0)
    val screenPosition: LiveData<Int>
        get() = _screenPosition

    val authRepository = AuthRepository(app)
    val syncManager = SyncManager(app)

    var tncData = MutableLiveData<String>().default("")


//    fun getTnc() = viewModelScope.async {
//        authRepository.getTnc { status, response, error ->
//            when(status){
//                true->{
//                    if (response != null) {
//                        tncData.value = response
//                    }
//                }
//            }
//        }
//    }



}