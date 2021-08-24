package com.emcash.customerapp.ui.loademcash
//https://stackoverflow.com/questions/18475826/how-to-perform-a-fade-animation-on-activity-transition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.*
import com.emcash.customerapp.model.DummyCardResponse
import com.emcash.customerapp.model.dummyAccounts
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.KEY_TOPUP_AMOUNT
import com.emcash.customerapp.utils.KEY_TOPUP_DESC
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : AppCompatActivity() {

    val viewModel: LoadEmCashViewModel by viewModels()

    val amount by lazy {
        intent.getIntExtra(KEY_TOPUP_AMOUNT, 0)
    }

    val desc by lazy {
        intent.getStringExtra(KEY_TOPUP_DESC)
    }

    val loader by lazy {
        LoaderDialog(this)
    }

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
            topup(amount,desc)
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

    private fun topup(amount:Int,desc:String?){
        if(amount>0){
            loader.showLoader()
            viewModel.addEmCash(amount,
                desc?:"",
                onFinished = {
                status, error ->
                when(status){
                    true->{
                        loader.hideLoader()
                        gotoWalletScreen()
                    }
                    false->{
                        loader.hideLoader()
                        showShortToast(error)
                    }
                }
            })

        }else{
            showShortToast("Invalid Amount.")
        }
    }

    private fun gotoWalletScreen() {
        openActivity(WalletActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}

