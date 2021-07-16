package com.emcash.customerapp.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.emcash.customerapp.model.dummyTransactionHistoryData

class TransactionHistoryViewModel(val app: Application):AndroidViewModel(app){

    val transactionHistory = dummyTransactionHistoryData

}