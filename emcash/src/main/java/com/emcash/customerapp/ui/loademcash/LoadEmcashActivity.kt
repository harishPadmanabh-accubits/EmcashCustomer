package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.extensions.toAmount
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_load_emcash.*
import java.math.BigDecimal
import java.math.RoundingMode

class LoadEmcashActivity : AppCompatActivity() {

    val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_HOME)
    }

    private val viewModel: LoadEmCashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_emcash)
        et_value.requestFocus()
        et_description.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                topupWallet()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }
        fab_done.setOnClickListener {
            topupWallet()

        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (source == SCREEN_WALLET) {
            openActivity(WalletActivity::class.java)
            finish()
        } else {
            openActivity(HomeActivity::class.java)
            finish()
        }
    }

    private fun topupWallet() {
        val amount = if ( et_value.text.toString().length>0) et_value.text.toString().toInt() else 0
        val desc = et_description.text.toString()
        if (amount > 0) {
            openActivity(TransactionActivity::class.java) {
                this.putDouble(KEY_TOPUP_AMOUNT, amount.toAmount())
                this.putString(KEY_TOPUP_DESC, desc)
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else {
            showShortToast("Enter the amount of EmCash to be Added.")
        }


    }


}