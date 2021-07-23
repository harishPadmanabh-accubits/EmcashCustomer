package com.emcash.customerapp.ui.convert_emcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.ui.home.HomeActivity
import com.emcash.customerapp.ui.loademcash.TransactionActivity
import kotlinx.android.synthetic.main.activity_convert_emcash.*
import kotlinx.android.synthetic.main.activity_convert_emcash.et_value

class ConvertEmcashActivity : AppCompatActivity(),SuccessDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_emcash)
        et_value.requestFocus()
        et_iban.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                SuccesDialog(this).show(supportFragmentManager,"Success")
                return@setOnEditorActionListener  true
            }
            return@setOnEditorActionListener  true
        }
    fab_done.setOnClickListener {
        SuccesDialog(this).show(supportFragmentManager,"Success")
    }
    }

    override fun onNavigate() {
        openActivity(HomeActivity::class.java)
        overridePendingTransition(android.R.anim.fade_out,android.R.anim.fade_in)
    }

}