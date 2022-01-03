package com.emcash.customerapp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.repos.AuthRepository
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import com.emcash.customerapp.ui.viewAllTransactions.pagingSource.ViewAllTransactionPagingSource
import kotlinx.coroutines.withContext

class HomeViewModel(app:Application):AndroidViewModel(app) {

    val syncManager = SyncManager(app)
    private val authRepository = AuthRepository(app)
    val homeRepository = HomeRepository(app)

    val profileDetails = authRepository.getProfileDetails()

    val allTransactedUsers = homeRepository.getAllTransactedUsers(viewModelScope)








}