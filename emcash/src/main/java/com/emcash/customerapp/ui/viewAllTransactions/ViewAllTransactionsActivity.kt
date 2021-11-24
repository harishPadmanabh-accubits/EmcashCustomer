package com.emcash.customerapp.ui.viewAllTransactions

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.home.HomeViewModel
import com.emcash.customerapp.ui.newPayment.NewPaymentActivity
import com.emcash.customerapp.ui.viewAllTransactions.adapter.AllTransactedUserItemClickListener
import com.emcash.customerapp.ui.viewAllTransactions.adapter.ViewAllTransactionsAdapterV2
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_view_all_transactions.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewAllTransactionsActivity : AppCompatActivity(),AllTransactedUserItemClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private val pagedAdapter by lazy {
        ViewAllTransactionsAdapterV2(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_transactions)
        setupViews()
        lifecycleScope.launch {
            viewModel.allTransactedUsers.collect {
                pagedAdapter.submitData(it)
            }
        }
    }

    private fun setupViews() {
        rv_all_transactions.adapter = pagedAdapter.also {
            it.addLoadStateListener { state ->
                if (state.source.refresh is LoadState.Error) {
                   showShortToast((state.source.refresh as LoadState.Error).error.message.toString())
                }
            }
        }
        iv_back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        openActivity(HomeActivity::class.java)
        finish()
    }

    override fun onTransactedUserClicked(user: RecentTransactionItem) {
        openActivity(NewPaymentActivity::class.java) {
            this.putInt(LAUNCH_DESTINATION, SCREEN_CHAT)
            this.putInt(LAUNCH_SOURCE, SCREEN_VIEW_ALL)
            this.putInt(KEY_BEN_ID, user.userId)
        }
        finish()
    }
}