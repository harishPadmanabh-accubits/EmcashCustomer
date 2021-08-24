package com.emcash.customerapp.ui.newPayment

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.data.repos.PaymentRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.*
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.utils.ITEM_ALL_CONTACTS
import com.emcash.customerapp.utils.ITEM_RECENT_CONTACTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber

class NewPaymentViewModel(val app: Application) : AndroidViewModel(app) {

    val paymentRepository = PaymentRepository(app)
    val homeRepository = HomeRepository(app)

    var _screen = MutableLiveData<NewPaymentScreens>().default(CONTACTS)
    val screens: LiveData<NewPaymentScreens> get() = _screen

    val validPin = "0000"

    fun gotoScreen(screen: NewPaymentScreens) {
        _screen.value = screen
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
            viewModelScope.async(Dispatchers.Default) {
                val result = processContactScreenItems(recentContacts, groupedContacts)
                withContext(Dispatchers.Main) {
                    _contactScreenItems.postValue(result)
                }
            }

        }

        _contactScreenItems.addSource(paymentRepository.getAllContacts()) {
            viewModelScope.async(Dispatchers.Default) {
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

    fun getRecentTransactions() {
        homeRepository.getRecentTransactions()

    }


}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT, PIN, SCAN
}