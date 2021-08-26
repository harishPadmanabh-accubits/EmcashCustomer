package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.afterTextChanged
import com.emcash.customerapp.extensions.toJson
import com.emcash.customerapp.model.ContactsPageItems
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.contacts.ContactItem
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.ui.newPayment.adapters.ContactsScreenAdapter
import com.emcash.customerapp.utils.KEY_SELECTED_CONTACT
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
                listenForQuerry(it)
                rv_contact_items.itemAnimator = DefaultItemAnimator()
                rv_contact_items.adapter =ContactsScreenAdapter(it,this@ContactsFragment)
            })
        }
    }

    private fun listenForQuerry(data: ArrayList<ContactsPageItems>) {
        et_search.afterTextChanged {querry->
            rv_contact_items.adapter =ContactsScreenAdapter(data,this@ContactsFragment).also {
                it.querry = querry
            }

        }

    }

    override fun onContactSelected(contact: DummyContactsRawData?, recentContact: DummyUserData?) {
        viewModel.gotoScreen(NewPaymentScreens.TRANSFER)
    }

    override fun onSelectedFromRecentContacts(contact: RecentTransactionItem) {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to contact.userId
        )
        viewModel.beneficiaryId = contact.userId.toInt()
        viewModel.gotoScreen(NewPaymentScreens.CHAT,bundle)


    }

    override fun onSelectedFromAllContacts(contact: ContactItem) {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to contact.id
        )
        viewModel.beneficiaryId = contact.id.toInt()
        viewModel.gotoScreen(NewPaymentScreens.CHAT,bundle)

    }
}