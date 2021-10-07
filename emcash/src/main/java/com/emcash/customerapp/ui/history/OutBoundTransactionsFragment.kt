package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.enums.TransactionHistoryScreens
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.DummyTransactionModel
import com.emcash.customerapp.model.transactions.HistoryFilter
import com.emcash.customerapp.ui.history.adapters.AllTransactionAdapter
import com.emcash.customerapp.ui.history.adapters.HistoryPagerAdapter
import com.emcash.customerapp.ui.history.adapters.TransactionFilter
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter
import com.emcash.customerapp.utils.LoaderDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_outbound_transactions.*
import timber.log.Timber

class OutBoundTransactionsFragment : Fragment(R.layout.layout_outbound_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }
    val pagedAdapter by lazy { HistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.e("on view created AllTransactionsFragment")
        rv_outbound.adapter = pagedAdapter
        viewModel.scope = lifecycleScope
        viewModel.filter.value = HistoryFilter(mode = "2")
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

        }
    }

    fun refresh() {
        Timber.e("Refreshed")
        viewModel.filter.value = HistoryFilter(mode = "2")
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentScreen = TransactionHistoryScreens.OUTBOUND
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