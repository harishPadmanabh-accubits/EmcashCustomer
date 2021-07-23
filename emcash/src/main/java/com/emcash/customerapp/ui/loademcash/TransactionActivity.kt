package com.emcash.customerapp.ui.loademcash
//https://stackoverflow.com/questions/18475826/how-to-perform-a-fade-animation-on-activity-transition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.hide
import com.emcash.customerapp.extensions.obtainViewModel
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.model.DummyCardResponse
import com.emcash.customerapp.model.dummyAccounts
import com.emcash.customerapp.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {

    val viewModel: LoadEmCashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rv_accounts.apply {
            adapter = AccountsAdapter(dummyAccounts)
        }

        tab_empay.setOnClickListener {
            viewModel._accountMode.value = AccountMode.EMPAY
        }
        tab_bank_card.setOnClickListener {
            viewModel._accountMode.value = AccountMode.BANK_CARD
        }

        btn_continue.setOnClickListener {
            openActivity(WalletActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel._accountMode.observe(this, Observer {
            when (it) {
                AccountMode.EMPAY -> selectEmpayTab()
                AccountMode.BANK_CARD -> selectBankTab()
            }
        })
    }

    private fun selectEmpayTab() {
        tab_empay.setBackgroundResource(R.drawable.blue_stroke_light_blue_fill_round_bg)
        iv_empay_selected.show()

        tab_bank_card.setBackgroundResource(R.drawable.grey_rounded_bg)
        iv_bank_selected.hide()

    }

    private fun selectBankTab() {
        tab_empay.setBackgroundResource(R.drawable.grey_rounded_bg)
        iv_empay_selected.hide()

        tab_bank_card.setBackgroundResource(R.drawable.blue_stroke_light_blue_fill_round_bg)
        iv_bank_selected.show()
    }
}

