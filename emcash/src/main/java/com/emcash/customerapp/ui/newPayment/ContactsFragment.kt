package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.model.users
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.ui.newPayment.adapters.ContactsScreenAdapter
import kotlinx.android.synthetic.main.layout_contacts_fragment.*

class ContactsFragment:Fragment(R.layout.layout_contacts_fragment),ContactsListener {

    private val viewModel:NewPaymentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        fab_scan.setOnClickListener {
            viewModel.gotoScreen(NewPaymentScreens.SCAN)
        }

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    private fun observe() {
        viewModel.apply {
            getContactScreenItems()
            contactScreenItems.observe(viewLifecycleOwner, Observer {
                rv_contact_items.adapter =ContactsScreenAdapter(it,this@ContactsFragment)
            })
        }
    }

    override fun onContactSelected(contact: DummyContactsRawData?, recentContact: DummyUserData?) {
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER)
    }

    override fun onSelectedFromRecentContacts(contact: RecentTransactionItem) {

    }

    override fun onSelectedFromAllContacts(contact: ContactItem) {
    }
}