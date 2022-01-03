package com.emcash.customerapp.ui.loadEmcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.extensions.toAmount
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.*
import kotlinx.android.synthetic.main.activity_load_emcash.*
import timber.log.Timber

class LoadEmcashActivity : AppCompatActivity() {

    val source by lazy {
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_HOME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_emcash)
        et_value.requestFocus()

        fab_done.setOnClickListener {
            topUpWallet()

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

    private fun topUpWallet() {
        val amount = if (et_value.text.toString().isNotEmpty()) et_value.text.toString().toInt() else 0
        val desc = et_description.text.toString()
        Timber.e("Load emcach amt ${amount.toAmount()}")
        if (amount > 0) {
            openActivity(TransactionActivity::class.java) {
                this.putString(KEY_TOPUP_AMOUNT, amount.toAmount())
                this.putString(KEY_TOPUP_DESC, desc)
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else {
            showShortToast(getString(R.string.error_load_emcash_empty))
        }


    }


}