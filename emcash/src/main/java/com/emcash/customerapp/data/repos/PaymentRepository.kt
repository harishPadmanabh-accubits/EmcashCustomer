package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.BlockedResponse
import com.emcash.customerapp.model.UnblockedResponse
import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.payments.*
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import timber.log.Timber

class PaymentRepository(private val context: Context) {
    private val syncManager = SyncManager(context)
    val api = EmCashApiManager(context).api
    fun getRecentTransactions(onCache:(data:RecentTransactionResponse.Data)->Unit): LiveData<RecentTransactionResponse.Data> {
        val _transactions = MutableLiveData<RecentTransactionResponse.Data>()
        api.getRecentTransactions()
            .awaitResponse(onSuccess = { response ->
                if (response != null) {
                    syncManager.recentTransactionsCache = response.data
                    _transactions.value = response.data
                }
            }, onFailure = { error ->
                Timber.e("Recent Contacts api error $error")
                syncManager.recentTransactionsCache?.let { onCache(it) }
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
            if (!it?.data?.referenceId.isNullOrEmpty()) {
                syncManager.initiatedRefId = it?.data?.referenceId
                onApiCallBack(true, it?.data?.referenceId, null)
            } else
                onApiCallBack(false, null, "Reference id null")
        }, onFailure = {
            onApiCallBack(false, null, it)
        })

    }

    fun transferAmount(onTransferCallBack: (status: Boolean, error: String?) -> Unit) {
        val refId = syncManager.initiatedRefId
        Timber.e("refid in repo for transfer $refId")
        if (!refId.isNullOrEmpty()) {
            val request = TransferRequest(refId)
            api.transferAmount(request).awaitResponse(onSuccess = {
                val status = it?.status ?: false
                onTransferCallBack(true, null)
            }, onFailure = {
                onTransferCallBack(false, it)
            })

        }
    }

    fun getTransactionDetails(
        refId: String,
        onApiCallBack: (status: Boolean, response: TransactionDetailsResponse.Data?, error: String?) -> Unit
    ) {
        api.getTransactionDetails(refId).awaitResponse(onSuccess = {
            if (it?.status == true)
                onApiCallBack(true, it?.data, null)
            else
                onApiCallBack(false, null, it?.message)
        }, onFailure = {
            onApiCallBack(false, null, it)
        })

    }

    fun getTransactions(
        userId: Int,
        onApiCallBack: (status: Boolean, response: TransactionHistoryResponse.Data?, error: String?) -> Unit
    ) {
        api.getTransactionHistoryAsync(userId, 1, 10).awaitResponse(onSuccess = {
            onApiCallBack(true, it?.data, null)

        }, onFailure = {
            onApiCallBack(false, null, it)

        })
    }

    fun requestPayment(
        paymentRequest: PaymentRequest,
        onApiCallBack: (status: Boolean, response: String?, error: String?) -> Unit
    ){
        api.requestPayment(paymentRequest).awaitResponse(onSuccess = {
            if (!it?.data?.referenceId.isNullOrEmpty()) {
                syncManager.initiatedRefId = it?.data?.referenceId
                onApiCallBack(true, it?.data?.referenceId, null)
            } else
                onApiCallBack(false, null, "Reference id null")
        }, onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun acceptPayment(
        onApiCallBack: (status: Boolean, error: String?) -> Unit
    ){
        val refId = syncManager.initiatedRefId
        if(refId.isNullOrEmpty()){
            onApiCallBack(false,"Invalid Transaction")
        }else{
            val request = PaymentApprovalRequest(refId)
            api.acceptPayment(request).awaitResponse(onSuccess = {
                Timber.e("Accept Response $it")
                if(it?.status == true)
                    onApiCallBack(true,null)
                else
                    onApiCallBack(false,it?.message)
            },onFailure = {
                onApiCallBack(false,it)
            })
        }

    }


    fun rejectPayment(
        onApiCallBack: (status: Boolean, error: String?) -> Unit
    ){
        val refId = syncManager.initiatedRefId
        if(refId.isNullOrEmpty()){
            onApiCallBack(false,"Invalid Transaction")
        }else{
            val request = PaymentApprovalRequest(refId)
            api.rejectPayment(request).awaitResponse(onSuccess = {
                if(it?.status == true)
                    onApiCallBack(true,null)
                else
                    onApiCallBack(false,it?.message)
            },onFailure = {
                onApiCallBack(false,it)
            })
        }

    }

    fun getQRResult(
        qrRequest: QRRequest,
        onApiCallBack: (status: Boolean,response:QRResponse.Data?, error: String?) -> Unit
    ){
        api.getQRResult(qrRequest).awaitResponse(onSuccess = {
            val data = it?.data
            data?.let {
                onApiCallBack(true,data,null)
            }
        },onFailure = {
            onApiCallBack(false,null, it)
        })
    }

    fun blockContact(
        userId: Int,
        onApiCallback: (status: Boolean, message: String?, result: BlockedResponse?) -> Unit
    ) {
        api.blockContact( userId).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var data = it
                data.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }

    fun unBlockContact(
        userId: Int,
        onApiCallback: (status: Boolean, message: String?, result: UnblockedResponse?) -> Unit
    ) {
        api.unBlockContact( userId).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var data = it
                data.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }

}