package com.emcash.customerapp.ui.convert_emcash

import android.icu.lang.UScript
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.loademcash.TransactionActivity
import com.emcash.customerapp.ui.wallet.WalletActivity
import kotlinx.android.synthetic.main.activity_convert_emcash.*
import kotlinx.android.synthetic.main.activity_convert_emcash.et_value

class ConvertEmcashActivity : AppCompatActivity(),SuccessDialogListener {

    private lateinit var dialog: SuccesDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_emcash)
        dialog =SuccesDialog(this,this)
        et_value.requestFocus()
        et_iban.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
              dialog.show(supportFragmentManager,"Success")
                return@setOnEditorActionListener  true
            }
            return@setOnEditorActionListener  true
        }
    fab_done.setOnClickListener {
        dialog.show(supportFragmentManager,"Success")
    }

        iv_back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        openActivity(WalletActivity::class.java)
    }

    override fun onNavigate() {
      onBackPressed()
        finish()
    }

}