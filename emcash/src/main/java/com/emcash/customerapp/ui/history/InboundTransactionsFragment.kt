package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.DummyTransactionModel
import com.emcash.customerapp.model.transactions.HistoryFilter
import com.emcash.customerapp.ui.history.adapters.AllTransactionAdapter
import com.emcash.customerapp.ui.history.adapters.HistoryPagerAdapter
import com.emcash.customerapp.ui.history.adapters.TransactionFilter
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.layout_all_transactions.*
import kotlinx.android.synthetic.main.layout_inbound_transactions.*
import timber.log.Timber

class InboundTransactionsFragment:Fragment(R.layout.layout_inbound_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }
    val pagedAdapter by lazy { HistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("on view created AllTransactionsFragment")
        viewModel.scope = lifecycleScope
        rv_inbound.adapter = pagedAdapter
        if(isVisible)
            refresh()
        observe()



    }

    private fun observe() {
        viewModel.apply {
            pagedTransactions.observe(viewLifecycleOwner, Observer {
                pagedAdapter.submitData(lifecycle, it)
            })

        }
    }


    fun refresh(){
        Timber.e("Refreshed")
        viewModel.filter.value = HistoryFilter(mode = "1")
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }


}