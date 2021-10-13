package com.emcash.customerapp.ui.convertEmcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.model.convertEmcash.UserBankAccountResponse
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawRequest

class ConvertEmcashViewModel(val app: Application) : AndroidViewModel(app) {
    val homeRepository = HomeRepository(app)
    var hasBankAccount = false

    fun withdraw(
        amount: Int,
        iban: String,
        onFinished: (status: Boolean, error: String?) -> Unit
    ) {

        homeRepository.withdrawFromWallet(
            WalletWithdrawRequest(amount, iban),
            onApiCallBack = { status, response, error ->
                when (status) {
                    true -> onFinished(true, null)
                    false -> onFinished(false, error)
                }
            })
    }

    fun getBankAccount(onResult: (result: ApiMapper<BankDetailsResponse>) -> Unit) {
        homeRepository.getBankDetailsForConvertEmcash { status, message, result ->
            when (status) {
                true -> onResult(ApiMapper(ApiCallStatus.SUCCESS, result, null))
                false -> onResult(ApiMapper(ApiCallStatus.ERROR, null, message))
            }

        }
    }

    fun addBankAccountDetails(
        addBankDetailsRequest: AddBankDetailsRequest?
    ): LiveData<ApiMapper<UserBankAccountResponse>> {
        val apiStatus = MutableLiveData<ApiMapper<UserBankAccountResponse>>()
        if (addBankDetailsRequest != null) {
            apiStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)
            homeRepository.addBankAccount(addBankDetailsRequest) { status, message, result ->
                when (status) {
                    true -> apiStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                    false -> apiStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)
                }
            }
        } else {
            apiStatus.value = ApiMapper(ApiCallStatus.ERROR, null, "Please Fill all fields.")
        }
        return apiStatus
    }

}