package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.emcash.customerapp.ui.history.adapters.AllTransactionAdapter
import com.emcash.customerapp.ui.history.adapters.HistoryPagerAdapter
import com.emcash.customerapp.ui.history.adapters.TransactionFilter
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.layout_all_transactions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllTransactionsFragment:Fragment(R.layout.layout_all_transactions) {

    private val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }
    val pagedAdapter by lazy { HistoryPagerAdapter() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_all_transactions.adapter = pagedAdapter

//        lifecycleScope.launch {
//            viewModel.getPagedTransactions("0", "", "", "", "").collect {
//                pagedAdapter.submitData(it)
//            }
//
//        }


//        lifecycleScope.launch {
//            viewModel.getPagedTransactions("0", "", "", "", "").observe(viewLifecycleOwner, Observer {
//                pagedAdapter.submitData(lifecycle,it)
//            })
//
//
//        }

        observe()
    }

    private fun observe() {
        viewModel.apply {

            pagedTransactions.observe(viewLifecycleOwner, Observer {
                pagedAdapter.submitData(lifecycle, it)
            })


//
//            status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//             //   viewModel.getAllTransactions("0","",it,"","")
////                lifecycleScope.launch {
////                    viewModel.getPagedTransactions("0","","",it,"").observe(viewLifecycleOwner, Observer {
////                        pagedAdapter.submitData(lifecycle,it)
////                    })
////
////
////                }
//
//
//
//
//            })
//            date.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//                viewModel.getAllTransactions("0","","",it[0], it[1])
//
//            })
//
//        }
        }
    }
}


