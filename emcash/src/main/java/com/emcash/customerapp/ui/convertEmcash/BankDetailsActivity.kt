package com.emcash.customerapp.ui.convertEmcash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.emcash.customerapp.R
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.utils.LoaderDialog
import kotlinx.android.synthetic.main.activity_bank_details.*

class BankDetailsActivity : AppCompatActivity() {
    private lateinit var dialog: SuccesDialog
    private val viewModel: ConvertEmcashViewModel by viewModels()
    private val loader by lazy { LoaderDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_details)
        setupViews()
    }

    fun setupViews(){
        iv_back.setOnClickListener {
            onBackPressed()
            finish()
        }

        btn_submit.setOnClickListener {
           updateBankAccount()

        }
    }

    private fun updateBankAccount() {
        viewModel.addBankAccountDetails(getBankDetailsRequestFromUI()).observe(this, Observer {
            when(it.status){
                ApiCallStatus.LOADING-> loader.showLoader()
                ApiCallStatus.SUCCESS->{
                    loader.hideLoader()
                    gotoConvertEmCashScreen()
                }
                ApiCallStatus.ERROR->{
                    loader.hideLoader()
                    showShortToast(it.errorMessage)
                }
            }
        })
    }

    private fun gotoConvertEmCashScreen() {
        openActivity(ConvertEmcashActivity::class.java)
        finish()
    }

    fun getBankDetailsRequestFromUI(): AddBankDetailsRequest? {
        val benficiaryName: String = et_benficiaryName.text.toString()
        val nickName: String = et_nickName.text.toString()
        val ibanNumber: String = et_ibanNumber.text.toString()
        val branchName: String = et_branchName.text.toString()
        val branchCode: String = et_branchCode.text.toString()
        val swiftCode: String = et_swiftCode.text.toString()
        if (benficiaryName.isEmpty() && ibanNumber.isEmpty() && branchName.isEmpty() && branchCode.isEmpty() && swiftCode.isEmpty()) {
            showShortToast("Please enter all the fields")
        }else{
            val addBankDetailsRequest = AddBankDetailsRequest(
                benficiaryName,
                branchName,
                ibanNumber,
                nickName,
                swiftCode,branchCode
            )
          return addBankDetailsRequest
        }
        return null
    }

}
