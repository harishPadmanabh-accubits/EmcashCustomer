package com.emcash.customerapp.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.terms.TermsAndConditionsActivity
import com.emcash.customerapp.ui.wallet.WalletActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    fun onClick(view: View) {
        when(view.id){
            R.id.iv_back->{
                onBackPressed()
            }
            R.id.cv_goto_wallet->{
                openActivity(WalletActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
            R.id.ll_terms->{
                openActivity(TermsAndConditionsActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        }
    }
}

