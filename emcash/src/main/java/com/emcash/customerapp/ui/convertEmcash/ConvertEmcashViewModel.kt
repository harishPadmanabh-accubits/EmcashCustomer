package com.emcash.customerapp.ui.convertEmcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.enums.BankAccountScreenTypes
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.model.convertEmcash.EditBankDetailsRequest
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
        addBankDetailsRequest: AddBankDetailsRequest?=null,editBankDetailsRequest: EditBankDetailsRequest?=null,type:BankAccountScreenTypes
    ): LiveData<ApiMapper<UserBankAccountResponse>> {
        val apiStatus = MutableLiveData<ApiMapper<UserBankAccountResponse>>()
            when(type){
                BankAccountScreenTypes.ADD->{
                    apiStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)
                    if (addBankDetailsRequest != null) {
                        homeRepository.addBankAccount(addBankDetailsRequest) { status, message, result ->
                            when (status) {
                                true -> apiStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                                false -> apiStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)
                            }
                        }
                    }
                }
                BankAccountScreenTypes.EDIT->{
                    apiStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)
                    if (editBankDetailsRequest != null) {
                        homeRepository.editBankAccount(editBankDetailsRequest) { status, message, result ->
                            when (status) {
                                true -> apiStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                                false -> apiStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)
                            }
                        }
                    }
                }
            }

        return apiStatus
    }

}