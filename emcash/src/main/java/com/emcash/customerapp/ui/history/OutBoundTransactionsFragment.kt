package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.model.DummyTransactionModel
import com.emcash.customerapp.ui.history.adapters.TransactionFilter
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter
import kotlinx.android.synthetic.main.layout_outbound_transactions.*

class OutBoundTransactionsFragment:Fragment(R.layout.layout_outbound_transactions) {

    private lateinit var viewModel: TransactionHistoryViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setupRecycelerView(viewModel.transactionHistory)
    }
    private fun setupRecycelerView(transactionHistory: List<DummyTransactionModel>) {
        rv_outbound.adapter = TransactionHistoryAdapter(transactionHistory).also {
            it.transactionType = TransactionFilter.OUTBOUND
        }
    }

    private fun getViewModel() {
        viewModel = requireActivity().obtainViewModel(TransactionHistoryViewModel::class.java)
    }
}