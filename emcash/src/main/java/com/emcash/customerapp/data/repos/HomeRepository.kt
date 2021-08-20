package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.transactions.RecentTransactionResponse

class HomeRepository(private val context: Context) {
    private val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api

    fun getRecentTransactions(): LiveData<ApiMapper<RecentTransactionResponse.Data>>{
        val _transactions = MutableLiveData<ApiMapper<RecentTransactionResponse.Data>>()
        _transactions.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        api.getRecentTransactions()
            .awaitResponse(onSuccess = {response->
            _transactions.value = ApiMapper(ApiCallStatus.SUCCESS,response?.data,null)
        },onFailure = {error->
            _transactions.value = ApiMapper(ApiCallStatus.ERROR,null,error)
        })
        return _transactions
    }


}