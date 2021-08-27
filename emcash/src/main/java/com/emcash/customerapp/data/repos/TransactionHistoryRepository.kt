package com.emcash.customerapp.data.repos

import android.content.Context
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.transactions.TransactionHistoryResponse

class TransactionHistoryRepository(val context: Context) {

    private val syncManager = SyncManager(context)
    val api = EmCashApiManager(context).api

    fun getAllTransactionHistory(
        mode: String, type: String, status: String, startDate: String, endDate: String,
        onApiCallback: (status: Boolean, message: String?, result: TransactionHistoryResponse.Data?) -> Unit
    ) {
        api.getTransactionHistory(
            1,
            1000,
            mode,
            startDate,
            endDate, status,
            type
        ).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {
                val data = it?.data
                data?.let {
                    onApiCallback(true, null, data)
                }
            })
    }

    fun getInboundTransaction(
        mode: String, type: String, status: String, startDate: String, endDate: String,
        onApiCallback: (status: Boolean, message: String?, result: TransactionHistoryResponse.Data?) -> Unit
    ) {
        api.getTransactionHistory(
            1,
            1000,
            mode,
            startDate,
            endDate, status,
            type
        ).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {
                val data = it?.data
                data?.let {
                    onApiCallback(true, null, data)
                }
            })
    }

    fun getOutboundTransaction(
        mode: String, type: String, status: String, startDate: String, endDate: String,
        onApiCallback: (status: Boolean, message: String?, result: TransactionHistoryResponse.Data?) -> Unit
    ) {
        api.getTransactionHistory(
            1,
            1000,
            mode,
            startDate,
            endDate, status,
            type
        ).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {
                val data = it?.data
                data?.let {
                    onApiCallback(true, null, data)
                }
            })
    }
}