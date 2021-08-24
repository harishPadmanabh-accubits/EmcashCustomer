package com.emcash.customerapp.ui.convert_emcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_convert_emcash.*
import kotlinx.android.synthetic.main.activity_convert_emcash.et_value
import kotlinx.android.synthetic.main.activity_convert_emcash.fab_done
import kotlinx.android.synthetic.main.activity_convert_emcash.iv_back

class ConvertEmcashActivity : AppCompatActivity(), SuccessDialogListener {

    private lateinit var dialog: SuccesDialog
    private val viewModel: WithdrawViewModel by viewModels()
    private val loader by lazy { LoaderDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_emcash)
        dialog = SuccesDialog(this, this)
        et_value.requestFocus()
        et_iban.setOnEditorActionListener { textView, action, keyEvent ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                withdraw()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
        }
        fab_done.setOnClickListener {
            withdraw()
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

    fun withdraw() {
        val amount =
            if (et_value.text.toString().length > 0) et_value.text.toString().toInt() else 0
        val iban = et_iban.text.toString()
        if (amount > 0) {
            loader.showLoader()
            viewModel.withdraw(
                amount,
                iban = if (iban.isNotEmpty()) iban else "",
                onFinished = { status, error ->
                    when (status) {
                        true -> {
                            loader.hideLoader()
                            showSuccessDialog()
                        }
                        false ->{
                            loader.hideLoader()
                            showShortToast(error)
                        }
                    }
                }
            )
        }else{
            loader.hideLoader()
            showShortToast("Invalid Amount")
        }


    }

    private fun showSuccessDialog() {
        dialog.show(supportFragmentManager, "Success")
    }

}