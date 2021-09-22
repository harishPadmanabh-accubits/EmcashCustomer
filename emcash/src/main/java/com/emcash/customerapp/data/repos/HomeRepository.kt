package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.bankCard.*
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import com.emcash.customerapp.model.wallet.topup.WalletTopupResponse
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawRequest
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawResponse

class HomeRepository(private val context: Context) {
    private val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api

    fun getRecentTransactions(): LiveData<ApiMapper<RecentTransactionResponse.Data>> {
        val _transactions = MutableLiveData<ApiMapper<RecentTransactionResponse.Data>>()
        _transactions.value = ApiMapper(ApiCallStatus.LOADING, null, null)
        api.getRecentTransactions()
            .awaitResponse(onSuccess = { response ->
                _transactions.value = ApiMapper(ApiCallStatus.SUCCESS, response?.data, null)
            }, onFailure = { error ->
                _transactions.value = ApiMapper(ApiCallStatus.ERROR, null, error)
            })
        return _transactions
    }

    fun topupWallet(
        topupRequest: WalletTopupRequest,
        onApiCallBack: (status: Boolean, response: WalletTopupResponse?, error: String?) -> Unit
    ) {
        api.topupWallet(topupRequest).awaitResponse(onSuccess = {
            onApiCallBack(true, it, null)
        }, onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun getProfile(onApiCallBack: (status: Boolean, response: ProfileDetailsResponse.Data?, error: String?) -> Unit) {
        api.getProfileDetails().awaitResponse(onSuccess = {
            onApiCallBack(true, it?.data, null)
        }, onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun withdrawFromWallet(
        withdrawRequest: WalletWithdrawRequest,
        onApiCallBack: (status: Boolean, response: WalletWithdrawResponse?, error: String?) -> Unit
    ) {
        api.withdrawFromWallet(withdrawRequest).awaitResponse(onSuccess = {
            onApiCallBack(true, it, null)
        },onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun bankCardsListing(onApiCallback: (status: Boolean, message: String?, result: BankCardsListingResponse.Data?) -> Unit){
        api.getBankCard().awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var  data=it?.data
                data?.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }
    fun paymentByExistingCard(paymentByExistingCardRequest: PaymentByExistingCardRequest, onApiCallback: (status: Boolean, message: String?, result: PaymentByExisitingCardResponse?) -> Unit){
        api.paymentByExistingCard(paymentByExistingCardRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var  data=it
                data?.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }
    fun paymentByNewCard(paymentByNewCardRequest: PaymentByNewCardRequest, onApiCallback: (status: Boolean, message: String?, result: PaymentByNewCardResponse?) -> Unit){
        api.paymentByNewCard(paymentByNewCardRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var  data=it
                data?.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }

}