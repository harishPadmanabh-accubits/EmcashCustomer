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
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.layout_all_transactions.*
import kotlinx.android.synthetic.main.layout_all_transactions.empty_view
import kotlinx.android.synthetic.main.layout_all_transactions.refresh_layout
import timber.log.Timber

class AllTransactionsFragment:Fragment(R.layout.layout_all_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }
    val pagedAdapter by lazy { HistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("on view created AllTransactionsFragment")
        rv_all_transactions.adapter = pagedAdapter
        viewModel.scope = viewLifecycleOwner.lifecycleScope
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
                refresh_layout.stopIfRefreshing()
            })

        }
    }
    fun refresh(){
        Timber.e("Refreshed")
        viewModel.filter.value = HistoryFilter(mode = "0")

    }

    override fun onResume() {
        super.onResume()
        viewModel.currentScreen = TransactionHistoryScreens.ALL
        refresh()
    }

    fun handleEmptyView(shouldShow: Boolean) {
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





}


