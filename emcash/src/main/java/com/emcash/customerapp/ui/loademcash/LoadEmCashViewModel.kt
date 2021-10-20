package com.emcash.customerapp.ui.loademcash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.repos.HomeRepository
import com.emcash.customerapp.extensions.default
import com.emcash.customerapp.model.bankCard.*
import com.emcash.customerapp.model.notifications.NotificationResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest

class LoadEmCashViewModel(val app: Application) : AndroidViewModel(app) {

    val homeRepository = HomeRepository(app)
    val syncManager = SyncManager(app)

    var _accountMode = MutableLiveData<AccountMode>().default(AccountMode.EMPAY)
    var _notifications = MutableLiveData<ApiMapper<NotificationResponse.Data>>()
    var bankCardsStatus = MutableLiveData<ApiMapper<BankCardsListingResponse.Data>>()
    var paymentByExistingCardStatus = MutableLiveData<ApiMapper<PaymentByExisitingCardResponse>>()
    var paymentByNewCardStatus = MutableLiveData<ApiMapper<PaymentByNewCardResponse>>()
    var payerAuthenticatorStatus = MutableLiveData<ApiMapper<PayerAuthenticatorResponse>>()

    fun addEmCash(
        amount: Int,
        desc: String,
        onFinished: (status: Boolean, error: String?) -> Unit
    ) {
        val topupRequest = WalletTopupRequest(amount, desc)
        homeRepository.topupWallet(topupRequest) { status, response, error ->
            onFinished(status, error)

        }
    }

    fun bankCardListing() {
        bankCardsStatus.value = ApiMapper(ApiCallStatus.LOADING, null, null)

        homeRepository.bankCardsListing() { status, message, result ->
            when (status) {
                true -> {
                    bankCardsStatus.value = ApiMapper(ApiCallStatus.SUCCESS, result, null)
                }
                false -> {
                    bankCardsStatus.value = ApiMapper(ApiCallStatus.ERROR, null, message)

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