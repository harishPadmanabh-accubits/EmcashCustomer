package com.emcash.customerapp.ui.terms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.extensions.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TermsViewModel(val app: Application) : AndroidViewModel(app) {
    val authRepository = AuthRepository(app)
    val syncManager = SyncManager(app)

    val tncData: LiveData<ApiMapper<String>> get() = authRepository._tncData


    fun getTnc() = authRepository.getTnc()


}

enum class TncStatus {
    ACCEPTED, REJECTED, NOT_SHOWN
}