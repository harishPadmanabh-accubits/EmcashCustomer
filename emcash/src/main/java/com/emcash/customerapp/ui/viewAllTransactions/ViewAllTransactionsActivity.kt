package com.emcash.customerapp.ui.viewAllTransactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.ui.viewAllTransactions.adapter.ViewAllTransactionsAdapterV2
import com.emcash.customerapp.ui.viewAllTransactions.viewModel.AllTransactionsViewModel
import kotlinx.android.synthetic.main.activity_view_all_transactions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewAllTransactionsActivity : AppCompatActivity() {

    private  val viewModel: AllTransactionsViewModel by viewModels()
    val pagedAdapter by lazy {
        ViewAllTransactionsAdapterV2()
    }
    lateinit var mLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_transactions)


        mLayoutManager = GridLayoutManager(this, 5)


        pagedAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
            }

            else {

                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                error?.let {
                    this.showShortToast(it.error.message)
                }
            }
        }

        rv_all_transactions.apply {
            adapter = pagedAdapter
            layoutManager = mLayoutManager
        }
        iv_back.setOnClickListener {

            finish()

        }


        lifecycleScope.launch {
            viewModel.viewAllActivities.collect {
                pagedAdapter.submitData(it)
            }

        }
    }
}