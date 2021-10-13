package com.emcash.customerapp.ui.convertEmcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.show
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.extensions.toJson
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.ui.wallet.WalletActivity
import com.emcash.customerapp.utils.KEY_EXISTING_ACCOUNT
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_convert_emcash.*

class ConvertEmcashActivity : AppCompatActivity(), SuccessDialogListener {

    private lateinit var dialog: SuccesDialog
    private val viewModel: ConvertEmcashViewModel by viewModels()
    private val loader by lazy { LoaderDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert_emcash)
        setupViews()
    }

    private fun setupViews() {
        dialog = SuccesDialog(this, this)
        et_value.requestFocus()
        getBankAccount()

        fab_done.setOnClickListener {
            if (viewModel.hasBankAccount)
                withdraw()
            else
                gotoAccountDetailsScreen(null)
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getBankAccount() {
        viewModel.getBankAccount { result ->
            when (result.status) {
                ApiCallStatus.SUCCESS -> {
                    val account = result.data
                    if (account?.data != null) {
                        showBankAccount(account.data)
                        viewModel.hasBankAccount = true
                    } else {
                        addBankAccount()
                        viewModel.hasBankAccount = false
                    }
                }
                ApiCallStatus.ERROR -> {
                    showShortToast(result.errorMessage)
                }
            }
        }
    }

    private fun addBankAccount() {
        fl_add_bank_card.apply {
            show()
            setOnClickListener {
                gotoAccountDetailsScreen(null)
            }
        }
    }

    private fun showBankAccount(account: BankDetailsResponse.Data) {
        tv_iban.text = account.iBanNumber
        tv_account_name.text = account.beneficiaryName
        fl_show_bank_account.apply {
            show()
            setOnClickListener {
                gotoAccountDetailsScreen(account)
            }
        }

    }

    private fun gotoAccountDetailsScreen(account: BankDetailsResponse.Data?) {
        if (account != null) {
            openActivity(BankDetailsActivity::class.java) {
                putString(KEY_EXISTING_ACCOUNT, account.toJson())
            }
        } else {
            openActivity(BankDetailsActivity::class.java)
        }
        finish()
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
        val iban = tv_iban.text.toString()
        if (amount > 0) {
            loader.showLoader()
            viewModel.withdraw(
                amount,
                iban = if (iban.isNotEmpty()) iban else "",
                onFinished = { status, error ->
                    when (status) {
                        true -> {
                            loader.hideLoader()
                            showSuccessDialog(amount)
                        }
                        false -> {
                            loader.hideLoader()
                            showShortToast(error)
                        }
                    }
                }
            )
        } else {
            loader.hideLoader()
            showShortToast("Invalid Amount")
        }


    }

    private fun showSuccessDialog(amount: Int) {
        dialog.amount = amount.toString().plus(" Emcash has been converted successfully")
        dialog.show(supportFragmentManager, "Success")
    }

}