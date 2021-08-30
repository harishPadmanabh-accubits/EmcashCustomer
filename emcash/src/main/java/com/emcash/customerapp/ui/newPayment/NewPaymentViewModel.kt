package com.emcash.customerapp.ui.newPayment

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.data.repos.PaymentRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.*
import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.payments.PaymentApprovalRequest
import com.emcash.customerapp.model.payments.PaymentRequest
import com.emcash.customerapp.model.payments.TransactionDetailsResponse
import com.emcash.customerapp.model.payments.TransactionHistoryResponse
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.ui.newPayment.adapters.TransactionHistoryPagingSource
import com.emcash.customerapp.utils.ITEM_ALL_CONTACTS
import com.emcash.customerapp.utils.ITEM_RECENT_CONTACTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewPaymentViewModel(val app: Application) : AndroidViewModel(app) {

    val paymentRepository = PaymentRepository(app)
    val syncManager =SyncManager(app)



    var _screenConfig = MutableLiveData<ScreenConfig>().default(ScreenConfig(CONTACTS))
    val screenConfig: LiveData<ScreenConfig> get() = _screenConfig

    val validPin = "0000"

    var beneficiaryId=0

    fun gotoScreen(screen: NewPaymentScreens, bundle: Bundle? = null) {
        if (bundle != null)
            _screenConfig.value = ScreenConfig(screen, bundle)
        else
            _screenConfig.value = ScreenConfig(screen)
    }


    var _bottomSheetVisiblity = MutableLiveData<Boolean>()
    val bottomSheetVisibility: LiveData<Boolean> get() = _bottomSheetVisiblity


    fun groupContactsByLetters(allContacts: ArrayList<ContactItem>): ArrayList<GroupedContacts> {
        val groupedContactsList = ArrayList<GroupedContacts>()
        val accessedLetters = ArrayList<String>()
        val firstLetters = allContacts.also {
            it.sortBy {
                it.name.first()
            }
        }.map {
            it.name.first()
        } as MutableList

        firstLetters.forEach {
            it.toUpperCase()
        }

        firstLetters.distinct().forEach { letter ->
            Timber.e("Letter $letter")
            val contacts = ArrayList<ContactItem>()
            val groupedContacts = allContacts.filter {
                it.name.first().equals(letter, ignoreCase = true)
            }
            if (groupedContacts.isNotEmpty()) {
                contacts.addAll(groupedContacts)
            }

            groupedContactsList.add(
                GroupedContacts(letter.toString(), ArrayList(groupedContacts))
            )

        }

        groupedContactsList.sortBy {
            it.letter
        }

        return groupedContactsList

    }

    val _contactScreenItems = MediatorLiveData<ArrayList<ContactsPageItems>>()
    val contactScreenItems: LiveData<ArrayList<ContactsPageItems>> get() = _contactScreenItems
    fun getContactScreenItems(

    ) {
        var recentContacts: ArrayList<RecentTransactionItem>? = null
        var groupedContacts: ArrayList<GroupedContacts>? = null
        _contactScreenItems.addSource(paymentRepository.getRecentTransactions()) {
            recentContacts = ArrayList(it.transactionList)
            viewModelScope.async(Dispatchers.IO) {
                val result = processContactScreenItems(recentContacts, groupedContacts)
                withContext(Dispatchers.Main) {
                    _contactScreenItems.postValue(result)
                }
            }

        }

        _contactScreenItems.addSource(paymentRepository.getAllContacts()) {
            viewModelScope.async(Dispatchers.IO) {
                val allContacts = ArrayList(it)
                groupedContacts = groupContactsByLetters(allContacts)
                val result = processContactScreenItems(recentContacts, groupedContacts)
                withContext(Dispatchers.Main) {
                    _contactScreenItems.postValue(result)
                }
            }

        }


    }

    private fun processContactScreenItems(
        recentContacts: ArrayList<RecentTransactionItem>?,
        allContacts: ArrayList<GroupedContacts>?
    ): ArrayList<ContactsPageItems> {
        val processedContacts = ArrayList<ContactsPageItems>()
        recentContacts?.let { contacts ->
            if (contacts.isNotEmpty()) {
                processedContacts.add(
                    ContactsPageItems("Recent Contacts", ITEM_RECENT_CONTACTS).also {
                        it.recentContactList = contacts
                    }
                )
            }
        }

        allContacts?.let { contacts ->
            if (contacts.isNotEmpty()) {
                processedContacts.add(
                    ContactsPageItems("All Contacts", ITEM_ALL_CONTACTS).also {
                        it.allContactList = contacts
                    }
                )
            }

        }
        return processedContacts
    }

    val _contact = MutableLiveData<ApiMapper<Contact>>()
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
        paymentRepository.initPayment(request,onApiCallBack = {
            status, response, error ->
            when(status){
                true-> onInit(true,response,null)
                false->onInit(false,null,error)
            }
        })
    }

    fun requestPayment(
        amount: Int,
        userId: Int,
        desc: String,
        onRequest: (status: Boolean, refId: String?, error: String?) -> Unit
    ){
        val request = PaymentRequest(amount, desc, userId)
        paymentRepository.requestPayment(request,onApiCallBack = {
                status, response, error ->
            when(status){
                true-> onRequest(true,response,null)
                false->onRequest(false,null,error)
            }
        })
    }

    val _transactionDetails=MutableLiveData<ApiMapper<TransactionDetailsResponse.Data>>()
    fun getTransactionDetails(refId: String):LiveData<ApiMapper<TransactionDetailsResponse.Data>>{
        _transactionDetails.value =  ApiMapper(ApiCallStatus.LOADING, null, null)
        paymentRepository.getTransactionDetails(refId,onApiCallBack = {
            status, response, error ->
            when(status){
                true-> _transactionDetails.value = ApiMapper(ApiCallStatus.SUCCESS,response,null)
                false->_transactionDetails.value = ApiMapper(ApiCallStatus.ERROR,null,error)
            }
        })
        return _transactionDetails
    }

    val transactionHistory = Pager(PagingConfig(pageSize = 20)) {
        TransactionHistoryPagingSource(
            paymentRepository.api,
            beneficiaryId
        )
    }.flow
        .cachedIn(viewModelScope)

    val _history=MutableLiveData<ApiMapper<TransactionHistoryResponse.Data>>()
    fun getHistory(): LiveData<ApiMapper<TransactionHistoryResponse.Data>> {
        _history.value = ApiMapper(ApiCallStatus.LOADING,null,null)
        paymentRepository.getTransactions(beneficiaryId){
            status, response, error ->
            when(status){
                true->_history.value = ApiMapper(ApiCallStatus.SUCCESS,response,null)
                false->_history.value = ApiMapper(ApiCallStatus.ERROR,null,error)
            }
        }
        return _history
    }

//    fun acceptPayment(transactionId:String, onResult: (status: Boolean, error: String?) -> Unit){
//        if(transactionId.isNotEmpty()){
//            val request = PaymentApprovalRequest(transactionId)
//            paymentRepository.acceptPayment(request){
//                status, error ->
//                when(status){
//                    true-> onResult(true,null)
//                    false-> onResult(false,error)
//                }
//            }
//        }
//
//    }
//
//    fun rejectPayment(transactionId:String, onResult: (status: Boolean, error: String?) -> Unit){
//        if(transactionId.isNotEmpty()){
//            val request = PaymentApprovalRequest(transactionId)
//            paymentRepository.rejectPayment(request){
//                    status, error ->
//                when(status){
//                    true-> onResult(true,null)
//                    false-> onResult(false,error)
//                }
//            }
//        }
//
//    }
}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT, PIN, SCAN
}

