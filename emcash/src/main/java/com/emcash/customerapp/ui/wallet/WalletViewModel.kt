package com.emcash.customerapp.ui.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.EmCashApiManager

class WalletViewModel(val app:Application):AndroidViewModel(app) {
    val api = EmCashApiManager(app).api
    val walletActivities = Pager(PagingConfig(10)){
        WalletActivityPagingSource(api)
    }.flow.cachedIn(viewModelScope)

    val profile = SyncManager(app).profileDetails
}