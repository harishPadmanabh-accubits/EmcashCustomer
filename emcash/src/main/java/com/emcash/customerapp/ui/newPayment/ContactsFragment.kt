package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.model.users
import com.emcash.customerapp.ui.newPayment.adapters.ContactsScreenAdapter
import kotlinx.android.synthetic.main.layout_contacts_fragment.*

class ContactsFragment:Fragment(R.layout.layout_contacts_fragment) {

    private val viewModel:NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val groupedContacts = viewModel.groupContactsByLetters(viewModel.contactsRaw)
        val recentContactList = users
        viewModel.getContactScreenItems(ArrayList(recentContactList),groupedContacts)
        observe()


    }

    private fun observe() {
        viewModel.apply {
            contactScreenItems.observe(viewLifecycleOwner, Observer {
                rv_contact_items.adapter =ContactsScreenAdapter(it)
            })
        }
    }
}