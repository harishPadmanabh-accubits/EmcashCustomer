package com.emcash.customerapp.ui.viewAllTransactions.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.ui.viewAllTransactions.pagingSource.ViewAllTransactionPagingSource

class AllTransactionsViewModel (val app: Application) : AndroidViewModel(app) {

    private val api = EmCashApiManager(app).api

    val viewAllActivities = Pager(PagingConfig(1)){
        ViewAllTransactionPagingSource(api)
    }.flow.cachedIn(viewModelScope)




}