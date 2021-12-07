package com.emcash.customerapp.data.repos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.emcash.customerapp.data.SyncManager
import com.emcash.customerapp.data.network.ApiCallStatus
import com.emcash.customerapp.data.network.ApiMapper
import com.emcash.customerapp.data.network.EmCashApiManager
import com.emcash.customerapp.extensions.awaitResponse
import com.emcash.customerapp.model.bankCard.*
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.model.convertEmcash.EditBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.UserBankAccountResponse
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.transactions.RecentTransactionItem
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import com.emcash.customerapp.model.wallet.topup.WalletTopupResponse
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawRequest
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawResponse
import com.emcash.customerapp.ui.viewAllTransactions.pagingSource.ViewAllTransactionPagingSource
import kotlinx.coroutines.CoroutineScope

class HomeRepository(private val context: Context) {
    val syncManager = SyncManager(context)
    private val api = EmCashApiManager(context).api


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
        api.getProfileDetails().awaitResponse(onSuccess = {response->
            response?.let {
                syncManager.profileDetails = it.data
                syncManager.recentTransactionsCache = it.data.recentTransactions
                onApiCallBack(true, it.data, null)
            }

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
        }, onFailure = {
            onApiCallBack(false, null, it)
        })
    }

    fun bankCardsListing(onApiCallback: (status: Boolean, message: String?, result: BankCardsListingResponse.Data?) -> Unit) {
        api.getBankCard().awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)

            }, onSuccess = {
                var data = it?.data
                data?.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }

    fun paymentByExistingCard(
        paymentByExistingCardRequest: PaymentByExistingCardRequest,
        onApiCallback: (status: Boolean, message: String?, result: PaymentByExisitingCardResponse?) -> Unit
    ) {
        api.paymentByExistingCard(paymentByExistingCardRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {
                var data = it
                data?.let {
                    onApiCallback(true, null, data)

                }
            }
        )
    }

    fun paymentByNewCard(
        paymentByNewCardRequest: PaymentByNewCardRequest,
        onApiCallback: (status: Boolean, message: String?, result: PaymentByNewCardResponse?) -> Unit
    ) {
        api.paymentByNewCard(paymentByNewCardRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {data->
                data?.let {
                    onApiCallback(true, null, data)
                }
            }
        )
    }

    fun getBankDetailsForConvertEmcash(
        onApiCallback: (status: Boolean, message: String?, result: BankDetailsResponse?) -> Unit
    ) {
        api.getBankDetails().awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = { data ->
                syncManager.uuid = data?.data?.userBankDetailsRefeId
                onApiCallback(true, null, data)
            })
    }

    fun addBankAccount(
        addBankDetailsRequest: AddBankDetailsRequest,
        onApiCallback: (status: Boolean, message: String?, result: UserBankAccountResponse?) -> Unit
    ) {
        api.addBankDetails(addBankDetailsRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = { data ->
                onApiCallback(true, null, data)
            }
        )
    }

    fun authenticatePayer(
        payerAuthenticatorRequest: PayerAuthenticatorRequest,
        onApiCallback: (status: Boolean, message: String?, result: PayerAuthenticatorResponse?) -> Unit
    ) {
        api.authenticatePayer(payerAuthenticatorRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = {

                onApiCallback(true, null, it)
            }
        )
    }

    fun editBankAccount(
        editBankDetailsRequest: EditBankDetailsRequest,
        onApiCallback: (status: Boolean, message: String?, result: UserBankAccountResponse?) -> Unit
    ) {
        api.editBankDetails(editBankDetailsRequest).awaitResponse(
            onFailure = {
                onApiCallback(false, it, null)
            }, onSuccess = { data ->
                onApiCallback(true, null, data)
            }
        )
    }

    fun getCurrentUUID() = syncManager.uuid

    fun getAllTransactedUsers(scope: CoroutineScope) = Pager(PagingConfig(1)) {
        ViewAllTransactionPagingSource(api)
    }.flow.cachedIn(scope)


}