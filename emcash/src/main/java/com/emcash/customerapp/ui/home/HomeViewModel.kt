package com.emcash.customerapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import kotlinx.coroutines.withContext

class HomeViewModel(app:Application):AndroidViewModel(app) {

    val syncManager = SyncManager(app)
    private val authRepository = AuthRepository(app)
    val homeRepository = HomeRepository(app)

    val profileDetails = authRepository.getProfileDetails()
    val recentTransactions = homeRepository.getRecentTransactions()







}