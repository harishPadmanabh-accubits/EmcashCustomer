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

class AllTransactionsFragment:Fragment(R.layout.layout_all_transactions) {

    private  val viewModel: TransactionHistoryViewModel by activityViewModels()
    private val loader by lazy { LoaderDialog(requireContext()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllTransactions("0","","","","")
        observe()
    }

    private fun observe() {
        viewModel.apply {
            alltransactionHistoryResponse.observe(requireActivity(), androidx.lifecycle.Observer {
                when (it.status) {
                    ApiCallStatus.LOADING -> {
                        loader.showLoader()
                    }
                    ApiCallStatus.SUCCESS -> {
                        loader.hideLoader()
                        it.data?.transactions.let {
                            val groupedTransactions = groupTransactionActivitiesByDate(ArrayList(it))
                            rv_all_transactions.apply {
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
                        loader.hideLoader()
                        requireActivity().showShortToast(it.errorMessage)

                    }
                }

            })





            status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                viewModel.getAllTransactions("0","",it,"","")

            })
            date.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                viewModel.getAllTransactions("0","","",it[0], it[1])

            })

        }
    }



}