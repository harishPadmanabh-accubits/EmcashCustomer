package com.emcash.customerapp.ui.loadEmcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.bankCard.*

class LoadEmCashViewModel(val app: Application) : AndroidViewModel(app) {

    private val homeRepository = HomeRepository(app)
    val syncManager = SyncManager(app)

    var _accountMode = MutableLiveData<AccountMode>().default(AccountMode.BANK_CARD)
    var bankCards = MutableLiveData<ApiMapper<BankCardsListingResponse.Data>>()
    var paymentByExistingCardStatus = MutableLiveData<ApiMapper<PaymentByExisitingCardResponse>>()
    var paymentByNewCardStatus = MutableLiveData<ApiMapper<PaymentByNewCardResponse>>()
    var payerAuthenticatorStatus = MutableLiveData<ApiMapper<PayerAuthenticatorResponse>>()

    fun getBankCards() {
        bankCards.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        homeRepository.bankCardsListing { status, message, result ->
            when (status) {
                true -> {
                    bankCards.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    bankCards.value = ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }

    fun paymentByExistingCard(paymentByExistingCardRequest: PaymentByExistingCardRequest) {
        paymentByExistingCardStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        homeRepository.paymentByExistingCard(paymentByExistingCardRequest) { status, message, result ->
            when (status) {
                true -> {
                    paymentByExistingCardStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    paymentByExistingCardStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }
    fun paymentByNewCard(paymentByNewCardRequest: PaymentByNewCardRequest) {
        paymentByNewCardStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        homeRepository.paymentByNewCard(paymentByNewCardRequest) { status, message, result ->
            when (status) {
                true -> {
                    paymentByNewCardStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    paymentByNewCardStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }

    fun authenticatePayer(paymentAuthenticatorRequest: PayerAuthenticatorRequest) {
        payerAuthenticatorStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)

        homeRepository.authenticatePayer(paymentAuthenticatorRequest) { status, message, result ->
            when (status) {
                true -> {
                    payerAuthenticatorStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    payerAuthenticatorStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)

                }
            }
        }
    }


}


enum class AccountMode {
    EMPAY, BANK_CARD
}