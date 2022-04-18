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
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.stopIfRefreshing
import com.emcash.customerapp.model.transactions.HistoryFilter
import com.emcash.customerapp.ui.history.adapters.AllHistoryPagerAdapter
import kotlinx.android.synthetic.main.item_transaction_item.*
import kotlinx.android.synthetic.main.item_transaction_item.view.*
import kotlinx.android.synthetic.main.layout_all_transactions.*
import kotlinx.android.synthetic.main.layout_all_transactions.empty_view
import kotlinx.android.synthetic.main.layout_all_transactions.refresh_layout
import kotlinx.android.synthetic.main.layout_inbound_transactions.*
import timber.log.Timber
import java.lang.Exception

class AllTransactionsFragment : Fragment(R.layout.layout_all_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val pagedAdapter by lazy { AllHistoryPagerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_all_transactions.adapter = pagedAdapter
        viewModel.scope = viewLifecycleOwner.lifecycleScope
        observe()
        refresh_layout.setOnRefreshListener {
            refresh(true)
            viewModel.isFromFilter=false
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
                refresh_layout.stopIfRefreshing()
            })
            rv_all_transactions.scrollToPosition(0)


        }
    }

    private fun refresh(isManual:Boolean = false) {
        Timber.e("Refreshed")
        if (!viewModel.isFromFilter || isManual)
            viewModel.filter.value = HistoryFilter(mode = "0")

    }

    override fun onResume() {
        super.onResume()
        viewModel.currentScreen = TransactionHistoryScreens.ALL
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
                rv_all_transactions.findContainingViewHolder(rv_transaction_details) as AllHistoryPagerAdapter.ViewHolder
            viewHolder.itemView.rv_transaction_details?.let {
                it.adapter = null
            }
            rv_all_transactions.adapter = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}


