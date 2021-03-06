package com.emcash.customerapp.ui.convertEmcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.enums.BankAccountScreenTypes
import com.emcash.customerapp.extensions.fromJson
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.model.convertEmcash.EditBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.UserBankAccountResponse
import com.emcash.customerapp.utils.KEY_EXISTING_ACCOUNT
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_bank_details.*
import java.lang.Exception

class BankDetailsActivity : AppCompatActivity() {
    private val viewModel: ConvertEmcashViewModel by viewModels()
    private val loader by lazy { LoaderDialog(this) }
    private val existingAccount by lazy {
        try {
            intent.getStringExtra(KEY_EXISTING_ACCOUNT)
                ?.fromJson(BankDetailsResponse.Data::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        setupViews()
    }

    private fun setupViews() {
        iv_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        btn_submit.setOnClickListener {
            updateBankAccount()

        }
        existingAccount?.let {
            et_benficiaryName.setText(it.beneficiaryName)
            et_nickName.setText(it.nickName)
            et_ibanNumber.setText(it.iBanNumber)
            et_branchName.setText(it.branchName)
            et_branchCode.setText(it.branchCode)
            et_swiftCode.setText(it.swiftCode)
            tv_heading.text = getString(R.string.edit_bank_details)
            tv_description.text = getString(R.string.edit_bank_details_Desc)
        }
    }

    private fun updateBankAccount() {
        val type =
            if (existingAccount == null) BankAccountScreenTypes.ADD else BankAccountScreenTypes.EDIT
        when (type) {
            BankAccountScreenTypes.ADD -> {
                val addBankDetailsRequest = getBankDetailsRequestFromUI()
                if (addBankDetailsRequest != null) {
                    viewModel.addBankAccountDetails(
                        addBankDetailsRequest = addBankDetailsRequest,
                        type = type
                    ).observe(this, Observer {
                        handleObservedResult(it)
                    })
                }
            }
            BankAccountScreenTypes.EDIT -> {
                val editBankDetailsRequest = getBankDetailsRequestWithUUIDFromUI()
                if (editBankDetailsRequest != null) {
                    viewModel.addBankAccountDetails(
                        editBankDetailsRequest = editBankDetailsRequest,
                        type = type
                    ).observe(this, Observer {
                        handleObservedResult(it)
                    })

                }

            }
        }

    }

    private fun gotoConvertEmCashScreen() {
        openActivity(ConvertEmcashActivity::class.java)
        finish()
    }

    private fun getBankDetailsRequestFromUI(): AddBankDetailsRequest? {
        val beneficiaryName: String = et_benficiaryName.text.toString()
        val nickName: String = et_nickName.text.toString()
        val ibanNumber: String = et_ibanNumber.text.toString()
        val branchName: String = et_branchName.text.toString()
        val branchCode: String = et_branchCode.text.toString()
        val swiftCode: String = et_swiftCode.text.toString()
        if (beneficiaryName.isEmpty() || ibanNumber.isEmpty() || branchName.isEmpty() || branchCode.isEmpty() || swiftCode.isEmpty())
            showShortToast("Please enter all the fields")
        else return AddBankDetailsRequest(
            beneficiaryName,
            branchName,
            ibanNumber,
            nickName,
            swiftCode, branchCode
        )
        return null
    }

    private fun getBankDetailsRequestWithUUIDFromUI(): EditBankDetailsRequest? {
        val benficiaryName: String = et_benficiaryName.text.toString()
        val nickName: String = et_nickName.text.toString()
        val ibanNumber: String = et_ibanNumber.text.toString()
        val branchName: String = et_branchName.text.toString()
        val branchCode: String = et_branchCode.text.toString()
        val swiftCode: String = et_swiftCode.text.toString()
        if (benficiaryName.isEmpty() || ibanNumber.isEmpty() || branchName.isEmpty() || branchCode.isEmpty() || swiftCode.isEmpty()) {
            showShortToast("Please enter all the fields")
        } else {
            return EditBankDetailsRequest(
                benficiaryName,
                branchName,
                ibanNumber,
                nickName,
                swiftCode,
                branchCode,
                viewModel.homeRepository.getCurrentUUID()
            )
        }
        return null
    }

    private fun handleObservedResult(result: ApiMapper<UserBankAccountResponse>) {
        when (result.status) {
            ApiCallStatus.LOADING -> loader.showLoader()
            ApiCallStatus.SUCCESS -> {
                loader.hideLoader()
                gotoConvertEmCashScreen()
            }
            ApiCallStatus.ERROR -> {
                loader.hideLoader()
                showShortToast(result.errorMessage)
            }
        }
    }

    override fun onBackPressed() {
       gotoConvertEmCashScreen()
    }
}
