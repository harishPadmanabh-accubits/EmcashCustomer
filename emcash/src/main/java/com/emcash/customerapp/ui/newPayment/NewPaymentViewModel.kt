package com.emcash.customerapp.ui.newPayment

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.data.repos.PaymentRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.*
import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.model.payments.*
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.model.transactions.TransferScreenUIModel
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.ui.newPayment.adapters.ContactsPagingSource
import com.emcash.customerapp.ui.newPayment.adapters.TransactionHistoryPagingSource
import com.emcash.customerapp.utils.DEFAULT_PAGE_CONFIG
import kotlinx.coroutines.launch
@Suppress("PropertyName")

class NewPaymentViewModel(val app: Application) : AndroidViewModel(app) {

    private val paymentRepository = PaymentRepository(app)
    val syncManager = SyncManager(app)


    private var _screenConfig = MutableLiveData<ScreenConfig>().default(ScreenConfig(CONTACTS))
    val screenConfig: LiveData<ScreenConfig> get() = _screenConfig

    val validPin = "0000"

    var beneficiaryId = 0

    fun gotoScreen(screen: NewPaymentScreens, bundle: Bundle? = null) {
        if (bundle != null)
            _screenConfig.value = ScreenConfig(screen, bundle)
        else
            _screenConfig.value = ScreenConfig(screen)
    }


    var _bottomSheetVisibility = MutableLiveData<Boolean>()
    val bottomSheetVisibility: LiveData<Boolean> get() = _bottomSheetVisibility

    private val _contact = MutableLiveData<ApiMapper<Contact>>()
    fun getContactDetails(id: Int): LiveData<ApiMapper<Contact>> {
        _contact.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        paymentRepository.getContactDetails(id, onApiCallBack = { status, response, error ->
            when (status) {
                true -> _contact.value = ApiMapper(ApiCallStatus.SUCCESS, response, null)
                false -> _contact.value = ApiMapper(ApiCallStatus.ERROR, null, error)
            }
        })
        return _contact
    }

    fun initPayment(
        amount: Int,
        userId: Int,
        desc: String,
        onInit: (status: Boolean, refId: String?, error: String?) -> Unit
    ) {
        val request = PaymentRequest(amount, desc, userId)
        paymentRepository.initPayment(request, onApiCallBack = { status, response, error ->
            when (status) {
                true -> onInit(true, response, null)
                false -> onInit(false, null, error)
            }
        })
    }

    fun requestPayment(
        amount: Int,
        userId: Int,
        desc: String,
        onRequest: (status: Boolean, refId: String?, error: String?) -> Unit
    ) {
        val request = PaymentRequest(amount, desc, userId)
        paymentRepository.requestPayment(request, onApiCallBack = { status, response, error ->
            when (status) {
                true -> onRequest(true, response, null)
                false -> onRequest(false, null, error)
            }
        })
    }

    private val _transactionDetails = MutableLiveData<ApiMapper<TransactionDetailsResponse.Data>>()
    fun getTransactionDetails(refId: String): LiveData<ApiMapper<TransactionDetailsResponse.Data>> {
        _transactionDetails.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        paymentRepository.getTransactionDetails(refId, onApiCallBack = { status, response, error ->
            when (status) {
                true -> _transactionDetails.value = ApiMapper(ApiCallStatus.SUCCESS, response, null)
                false -> _transactionDetails.value = ApiMapper(ApiCallStatus.ERROR, null, error)
            }
        })
        return _transactionDetails
    }

    val _refreshChat = MutableLiveData<Boolean>()
    val pagedHistoryItems = Transformations.switchMap(_refreshChat) {
        Pager(PagingConfig(pageSize = DEFAULT_PAGE_CONFIG)) {
            TransactionHistoryPagingSource(
                paymentRepository.api,
                beneficiaryId
            )
        }.liveData.cachedIn(viewModelScope)
    }


    private val _history = MutableLiveData<ApiMapper<TransactionHistoryResponse.Data>>()
    fun getHistory(): LiveData<ApiMapper<TransactionHistoryResponse.Data>> {
        _history.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        paymentRepository.getTransactions(beneficiaryId) { status, response, error ->
            when (status) {
                true -> _history.value = ApiMapper(ApiCallStatus.SUCCESS, response, null)
                false -> _history.value = ApiMapper(ApiCallStatus.ERROR, null, error)
            }
        }
        return _history
    }

    fun onQrScanResult(
        refId: String,
        onResult: (status: Boolean, profile: QRResponse.Data?, error: String?) -> Unit
    ) {
        val request = QRRequest(refId)
        paymentRepository.getQRResult(request) { status, response, error ->
            when (status) {
                true -> onResult(true, response, null)
                false -> onResult(false, null, error)
            }
        }
    }


    val searchQuery = MutableLiveData<String>().default("")
    val pagedAllContactList = Transformations.switchMap(searchQuery) {
        Pager(PagingConfig(DEFAULT_PAGE_CONFIG)) {
            ContactsPagingSource(EmCashApiManager(app).api, it)
        }.liveData.cachedIn(viewModelScope)
    }

    val recentContacts = paymentRepository.getRecentTransactions {
    }

    val recentContactsCache = syncManager.recentTransactionsCache

    fun blockAccountAsync(userId: Int,onResult: (status: Boolean, error: String?) -> Unit){
        viewModelScope.launch {
                paymentRepository.blockContactAsync(userId){status, message ->
                    if (status != null) {
                        onResult(status,message)
                    }
            }
        }
    }

    fun unblockAccount(userId: Int, onResult: (status: Boolean, error: String?) -> Unit) {
        paymentRepository.unBlockContact(userId) { status, message, result ->
            when (status) {
                true -> {
                    result?.status?.let { onResult(it, message) }
                }
                false -> {
                    onResult(false, message)
                }
            }
        }
    }

    fun cacheTransferScreenData(amount: Int, userId: Int, desc: String) {
        viewModelScope.launch {
            val transferScreenUIModel=TransferScreenUIModel(
                userId = userId,
                amount = amount,
                desc = desc
            )
            syncManager.transferScreenCache = transferScreenUIModel
        }
    }

    fun getTransferScreenCache() = syncManager.transferScreenCache

    fun clearTransferScreenCache() {
        viewModelScope.launch {
            syncManager.transferScreenCache=null
        }
    }


}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT, PIN, SCAN
}

