package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.payments.PaymentRequest
import com.emcash.customerapp.model.payments.TransactionDetails
import com.emcash.customerapp.model.payments.TransferRequest
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import timber.log.Timber

class PaymentRepository(private val context: Context) {
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

    fun getAllContacts(): LiveData<List<ContactItem>> {
        val _contacts = MutableLiveData<List<ContactItem>>()
        api.getAllContacts("", 1, 50).awaitResponse(onSuccess = {
            if (it != null) {
                _contacts.value = it.data.contactList
            }
        }, onFailure = {
            Timber.e("All Contacts api error $it")
        })
        return _contacts
    }

    fun getContactDetails(
        id: Int,
        onApiCallBack: (status: Boolean, response: Contact?, error: String?) -> Unit
    ) {
        api.getContactDetails(id).awaitResponse(onSuccess = {
            val contactDetails = it?.contact
            onApiCallBack(true, contactDetails, null)
        }, onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun initPayment(
        paymentRequest: PaymentRequest,
        onApiCallBack: (status: Boolean, response: String?, error: String?) -> Unit
    ) {
        api.initPayment(paymentRequest).awaitResponse(onSuccess = {
            if (!it?.data?.referenceId.isNullOrEmpty()){
                syncManager.initiatedRefId =it?.data?.referenceId
                onApiCallBack(true, it?.data?.referenceId, null)
            }
            else
                onApiCallBack(false, null, "Reference id null")
        }, onFailure = {
            onApiCallBack(false, null, it)
        })

    }

    fun transferAmount(onTransferCallBack:(status:Boolean,error:String?)->Unit){
        val refId = syncManager.initiatedRefId
        Timber.e("refid in repo for transfer $refId")
        if(!refId.isNullOrEmpty()){
            val request = TransferRequest(refId)
            api.transferAmount(request).awaitResponse(onSuccess = {
                val status = it?.status ?: false
                onTransferCallBack(true,null)
            },onFailure = {
                onTransferCallBack(false,it)
            })

        }
    }

    fun getTransactionDetails(refId:String,onApiCallBack: (status: Boolean, response: TransactionDetails.Data?, error: String?) -> Unit){
        api.getTransactionDetails(refId).awaitResponse(onSuccess = {
            if(it?.status==true)
                onApiCallBack(true,it?.data,null)
            else
                onApiCallBack(false,null,it?.message)
        },onFailure = {
            onApiCallBack(false,null,it)
        })

    }

}