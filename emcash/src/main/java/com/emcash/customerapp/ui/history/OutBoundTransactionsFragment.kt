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
import com.emcash.customerapp.ui.history.adapters.HistoryPagerAdapter
import kotlinx.android.synthetic.main.item_transaction_item.*
import kotlinx.android.synthetic.main.item_transaction_item.view.*
import kotlinx.android.synthetic.main.layout_outbound_transactions.*
import kotlinx.android.synthetic.main.layout_outbound_transactions.empty_view
import kotlinx.android.synthetic.main.layout_outbound_transactions.refresh_layout
import java.lang.Exception

class OutBoundTransactionsFragment : Fragment(R.layout.layout_outbound_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val pagedAdapter by lazy { HistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scope = viewLifecycleOwner.lifecycleScope
        viewModel.filter.value = HistoryFilter(mode = "2")
        observe()
        refresh_layout.setOnRefreshListener {
            refresh()
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
            refresh_layout.stopIfRefreshing()

        }
    }

    private fun refresh() {
        rv_outbound.adapter = pagedAdapter
        viewModel.filter.value = HistoryFilter(mode = "2")
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentScreen = TransactionHistoryScreens.OUTBOUND
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
                rv_outbound.findContainingViewHolder(rv_transaction_details) as HistoryPagerAdapter.ViewHolder
            viewHolder.itemView.rv_transaction_details?.let {
                it.adapter = null
            }
            rv_outbound.adapter = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}