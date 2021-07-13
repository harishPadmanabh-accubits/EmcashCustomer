package com.emcash.customerapp.ui.loademcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import kotlinx.android.synthetic.main.activity_load_emcash.*

class LoadEmcashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_emcash)
        et_value.requestFocus()
        et_description.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                openActivity(TransactionActivity::class.java)
              return@setOnEditorActionListener  true
            }
            return@setOnEditorActionListener  true
        }
    }
}