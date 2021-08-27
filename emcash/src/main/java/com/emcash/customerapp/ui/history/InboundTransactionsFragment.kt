package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.DummyTransactionModel
import com.emcash.customerapp.ui.history.adapters.AllTransactionAdapter
import com.emcash.customerapp.ui.history.adapters.TransactionFilter
import com.emcash.customerapp.ui.history.adapters.TransactionHistoryAdapter
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.layout_all_transactions.*
import kotlinx.android.synthetic.main.layout_inbound_transactions.*

class InboundTransactionsFragment:Fragment(R.layout.layout_inbound_transactions) {

    private  val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getInBoundTransactions("1","","","","")
        observe()

    }

    private fun observe() {
        viewModel.apply {
            status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                viewModel.getInBoundTransactions("1","",it,"","")

            })
            date.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                viewModel.getInBoundTransactions("1","","",it[0], it[1])

            })
            inBoundtransactionHistoryResponse.observe(requireActivity(), androidx.lifecycle.Observer {
                when (it.status) {
                    ApiCallStatus.LOADING -> {
                    }
                    ApiCallStatus.SUCCESS -> {
                        it.data?.transactions.let {
                            val groupedTransactions = groupTransactionActivitiesByDate(ArrayList(it))
                            rv_inbound.apply {
                                layoutManager = LinearLayoutManager(
                                    requireActivity(),
                                    RecyclerView.VERTICAL, false
                                )
                                itemAnimator = DefaultItemAnimator()
                                adapter = AllTransactionAdapter(
                                    groupedTransactions
                                )
                            }
                        }


                    }

                    ApiCallStatus.ERROR -> {
                        requireActivity().showShortToast(it.errorMessage)

                    }
                }

            })
        }
    }


}