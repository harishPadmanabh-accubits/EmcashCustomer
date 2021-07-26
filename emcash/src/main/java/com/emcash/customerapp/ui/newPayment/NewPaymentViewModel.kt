package com.emcash.customerapp.ui.newPayment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.*
import com.emcash.customerapp.ui.newPayment.NewPaymentScreens.*
import com.emcash.customerapp.utils.ITEM_ALL_CONTACTS
import com.emcash.customerapp.utils.ITEM_RECENT_CONTACTS

class NewPaymentViewModel(val app: Application) : AndroidViewModel(app) {

    var _screen = MutableLiveData<NewPaymentScreens>().default(CONTACTS)
    val screens: LiveData<NewPaymentScreens> get() = _screen

    val validPin = "0000"

    var contactsRaw = dummyContactList
    fun gotoScreen(screen:NewPaymentScreens){
        _screen.value = screen
    }

    fun groupContactsByLetters(allContacts:ArrayList<DummyContactsRawData>): ArrayList<GroupedContacts> {
        val groupedContactsList = ArrayList<GroupedContacts>()
        val accessedLetters = ArrayList<String>()
        val firstLetters = allContacts.also {
            it.sortBy {
                it.name.first()
            }
        }.distinct().map {
            it.name.first()
        }

        firstLetters.forEach { letter->
            val contacts = ArrayList<DummyContactsRawData>()
            val groupedContacts = allContacts.filter {
                it.name.first() == letter
            }
            if(groupedContacts.isNotEmpty()){
                contacts.addAll(groupedContacts)
            }

            groupedContactsList.add(
                GroupedContacts(letter.toString(), ArrayList(groupedContacts))
            )

        }

        return groupedContactsList

    }
    val _contactScreenItems = MediatorLiveData<ArrayList<ContactsPageItems>>()
    val contactScreenItems:LiveData<ArrayList<ContactsPageItems>> get() = _contactScreenItems
    fun getContactScreenItems(recentContacts: ArrayList<DummyUserData>?,allContacts : ArrayList<GroupedContacts>?) {
        val result = processContactScreenItems(recentContacts,allContacts)
        _contactScreenItems.postValue(result)
    }

    private fun processContactScreenItems(recentContacts: ArrayList<DummyUserData>?, allContacts: ArrayList<GroupedContacts>?): ArrayList<ContactsPageItems> {
     val processedContacts = ArrayList<ContactsPageItems>()
        recentContacts?.let {contacts->
            if(contacts.isNotEmpty()){
                processedContacts.add(
                    ContactsPageItems("Recent Contacts", ITEM_RECENT_CONTACTS).also {
                        it.recentContactList = contacts
                    }
                )
            }
        }

        allContacts?.let { contacts->
            if(contacts.isNotEmpty()){
                processedContacts.add(
                    ContactsPageItems("All Contacts", ITEM_ALL_CONTACTS).also {
                        it.allContactList = contacts
                    }
                )
            }

        }
        return processedContacts
    }


}

enum class NewPaymentScreens {
    CONTACTS, TRANSFER, CHAT, RECEIPT,PIN
}