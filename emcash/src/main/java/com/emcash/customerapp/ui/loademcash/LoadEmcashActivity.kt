package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LAUNCH_SOURCE
import com.emcash.customerapp.utils.SCREEN_HOME
import com.emcash.customerapp.utils.SCREEN_WALLET
import kotlinx.android.synthetic.main.activity_load_emcash.*

class LoadEmcashActivity : AppCompatActivity() {

    val source by lazy{
        intent.getIntExtra(LAUNCH_SOURCE, SCREEN_HOME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_emcash)
        et_value.requestFocus()
        et_description.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                openActivity(TransactionActivity::class.java)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                return@setOnEditorActionListener  true
            }
            return@setOnEditorActionListener  true
        }
        fab_done.setOnClickListener {
            openActivity(TransactionActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if(source== SCREEN_WALLET)
        {
            openActivity(WalletActivity::class.java)
            finish()
        }else{
            openActivity(HomeActivity::class.java)
            finish()
        }
    }


}