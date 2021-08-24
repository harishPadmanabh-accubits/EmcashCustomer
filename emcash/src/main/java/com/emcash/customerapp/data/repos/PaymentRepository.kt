package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import timber.log.Timber

class PaymentRepository(private val context: Context)  {
    private val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api
    fun getRecentTransactions(): LiveData<RecentTransactionResponse.Data> {
        val _transactions = MutableLiveData<RecentTransactionResponse.Data>()
        api.getRecentTransactions()
            .awaitResponse(onSuccess = { response ->
                if (response != null) {
                    _transactions.value = response.data
                }
            }, onFailure = { error ->
                Timber.e("Recent Contacts api error $error")
            })

        return _transactions
    }

    fun getAllContacts():LiveData<List<ContactItem>>{
        val _contacts = MutableLiveData<List<ContactItem>>()
        api.getAllContacts("",1,50).awaitResponse(onSuccess = {
            if(it!=null){
                _contacts.value = it.data.contactList
            }
        },onFailure = {
            Timber.e("All Contacts api error $it")
        })
        return _contacts
    }


}