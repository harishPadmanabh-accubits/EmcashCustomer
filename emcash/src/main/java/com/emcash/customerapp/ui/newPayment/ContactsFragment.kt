package com.emcash.customerapp.ui.newPayment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.afterTextChanged
import com.emcash.customerapp.model.ContactsPageItems
import com.emcash.customerapp.model.DummyContactsRawData
import com.emcash.customerapp.model.DummyUserData
import com.emcash.customerapp.model.contacts.ContactsGroup
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.home.adapter.RecentTransactionsAdapter
import com.emcash.customerapp.ui.newPayment.adapters.ContactsListener
import com.emcash.customerapp.ui.newPayment.adapters.ContactsPagedAdapter
import com.emcash.customerapp.utils.KEY_SELECTED_CONTACT
import com.emcash.customerapp.utils.SCREEN_CONTACTS
import kotlinx.android.synthetic.main.layout_contacts_fragment.*
import kotlinx.android.synthetic.main.row_contacts.*
import kotlinx.android.synthetic.main.row_recent_contacts.*

class ContactsFragment:Fragment(R.layout.layout_contacts_fragment),ContactsListener {

    private val viewModel:NewPaymentViewModel by activityViewModels()
    private val allContactsAdapter by lazy {
        ContactsPagedAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observe()
        listenForQuerry()


    }

    private fun setupViews() {
        fab_scan.setOnClickListener {
            viewModel.gotoScreen(NewPaymentScreens.SCAN)
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        rv_contacts.adapter = allContactsAdapter
    }

    private fun observe() {
        viewModel.apply {
            recentContacts.observe(viewLifecycleOwner, Observer {
             val adapter =    RecentTransactionsAdapter(it.transactionList.toList(),this@ContactsFragment).also {
                    it.source = SCREEN_CONTACTS
                }
              rv_recent_contacts.adapter = adapter
            })

            pagedAllContactList.observe(viewLifecycleOwner, Observer {
                allContactsAdapter.submitData(lifecycle,it)
            })
        }

    }

    private fun listenForQuerry() {
        et_search.afterTextChanged {querry->
            viewModel.searchQuerry.value = querry


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

    override fun onSelectedFromAllContacts(contact: ContactsGroup.ContactInfo) {
        val bundle = bundleOf(
            KEY_SELECTED_CONTACT to contact.id
        )
        viewModel.beneficiaryId = contact.id.toInt()
        viewModel.gotoScreen(NewPaymentScreens.CHAT,bundle)

    }
}