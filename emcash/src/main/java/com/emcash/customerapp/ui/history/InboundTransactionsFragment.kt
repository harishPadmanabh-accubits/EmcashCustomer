package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.emcash.customerapp.R
import com.emcash.customerapp.enums.TransactionHistoryScreens
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.transactions.HistoryFilter
import com.emcash.customerapp.ui.history.adapters.AllHistoryPagerAdapter
import com.emcash.customerapp.ui.history.adapters.InboundHistoryPagerAdapter
import kotlinx.android.synthetic.main.item_transaction_item.*
import kotlinx.android.synthetic.main.item_transaction_item.view.*
import kotlinx.android.synthetic.main.layout_inbound_transactions.*
import kotlinx.android.synthetic.main.layout_inbound_transactions.empty_view
import kotlinx.android.synthetic.main.layout_inbound_transactions.refresh_layout
import timber.log.Timber
import java.lang.Exception

class InboundTransactionsFragment : Fragment(R.layout.layout_inbound_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val pagedAdapter by lazy { InboundHistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("on view created AllTransactionsFragment")
        viewModel.scope = viewLifecycleOwner.lifecycleScope
        rv_inbound.adapter = pagedAdapter
        if (isVisible)
            refresh()
        observe()
        refresh_layout.setOnRefreshListener {
            refresh()
            refresh_layout.isRefreshing = false
        }
        pagedAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && pagedAdapter.itemCount < 1) {
                handleEmptyView(true)
            } else {
                handleEmptyView(false)
            }
        }


    }

    private fun observe() {
        viewModel.apply {
            pagedTransactions.observe(viewLifecycleOwner, Observer {
                pagedAdapter.submitData(lifecycle, it)
            })
            rv_inbound.scrollToPosition(0)
            refresh_layout.stopIfRefreshing()
        }
    }


    private fun refresh() {
        Timber.e("Refreshed")
        viewModel.filter.value = HistoryFilter(mode = "1")
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentScreen = TransactionHistoryScreens.INBOUND
        refresh()
    }

    private fun handleEmptyView(shouldShow: Boolean) {
        if (shouldShow) {
            refresh_layout.hide()
            empty_view.show()
            if (viewModel.isFilterByDate)
                empty_view.setDescription(getString(R.string.empty_transactions_date))
            else
                empty_view.setDescription(getString(R.string.empty_transactions))
        } else {
            refresh_layout.show()
            empty_view.hide()
        }
    }

    override fun onDestroyView() {
        clearAdapterInstances()
        super.onDestroyView()
    }

    private fun clearAdapterInstances() {
        try {
            val viewHolder =
                rv_inbound.findContainingViewHolder(rv_transaction_details) as AllHistoryPagerAdapter.ViewHolder
            viewHolder.itemView.rv_transaction_details?.let {
                it.adapter = null
            }
            rv_inbound.adapter = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}