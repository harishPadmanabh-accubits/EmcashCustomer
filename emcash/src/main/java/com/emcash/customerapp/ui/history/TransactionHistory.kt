package com.emcash.customerapp.ui.history

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.history.adapters.TransactionsTabAdapter
import com.emcash.customerapp.ui.home.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_transaction_history.*

class TransactionHistory : FragmentActivity() {

    private lateinit var viewModel: TransactionHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        getViewModel()
        setupTabs()
        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getViewModel() {
        viewModel = obtainViewModel(TransactionHistoryViewModel::class.java)
    }

    private fun setupTabs() {
        viewpager_tabs.adapter =
            TransactionsTabAdapter(
                this
            )
        TabLayoutMediator(tab_layout, viewpager_tabs) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all)
                1 -> tab.text = getString(R.string.inbound)
                2 -> tab.text = getString(R.string.outbound)
            }
        }.attach()

    }

    override fun onBackPressed() {
        openActivity(HomeActivity::class.java)
        openActivity(HomeActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}