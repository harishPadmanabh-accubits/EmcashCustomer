package com.emcash.customerapp.ui.newPayment.adapters

import com.emcash.customerapp.model.contacts.ContactsGroup
import com.emcash.customerapp.model.transactions.RecentTransactionItem

interface ContactsListener{
    fun onSelectedFromRecentContacts(contact: RecentTransactionItem)
    fun onSelectedFromAllContacts(contact: ContactsGroup.ContactInfo)
    fun onSelectedViewAllTransactions()

}