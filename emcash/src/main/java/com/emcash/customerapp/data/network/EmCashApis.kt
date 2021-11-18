package com.emcash.customerapp.data.network

import com.emcash.customerapp.model.BlockedResponse
import com.emcash.customerapp.model.UnblockedResponse
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountRequest
import com.emcash.customerapp.model.auth.switchAccount.SwitchAccountResponse
import com.emcash.customerapp.model.auth.userExists.UserExistCheckRequest
import com.emcash.customerapp.model.auth.userExists.UserExistCheckResponse
import com.emcash.customerapp.model.bankCard.*
import com.emcash.customerapp.model.contacts.AllContactsResponse
import com.emcash.customerapp.model.contacts.ContactDetails
import com.emcash.customerapp.model.contacts.ContactsGroupResponse
import com.emcash.customerapp.model.convertEmcash.AddBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.BankDetailsResponse
import com.emcash.customerapp.model.convertEmcash.EditBankDetailsRequest
import com.emcash.customerapp.model.convertEmcash.UserBankAccountResponse
import com.emcash.customerapp.model.notifications.NotificationResponse
import com.emcash.customerapp.model.payments.*
import com.emcash.customerapp.model.profile.ProfileDetailsResponse
import com.emcash.customerapp.model.tnc.TncResponse
import com.emcash.customerapp.model.transactions.RecentTransactionResponse
import com.emcash.customerapp.model.transactions.TransactionHistoryGroupResponse
import com.emcash.customerapp.model.wallet.WalletActivityGroupResponse
import com.emcash.customerapp.model.wallet.WalletActivityResponse
import com.emcash.customerapp.model.wallet.topup.WalletTopupRequest
import com.emcash.customerapp.model.wallet.topup.WalletTopupResponse
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawRequest
import com.emcash.customerapp.model.wallet.withdraw.WalletWithdrawResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface EmCashApis {

    @POST("v1/customers/auth/usercheck")
    fun checkIfUserExist(@Body checkRequest: UserExistCheckRequest): Call<UserExistCheckResponse>

    @POST("v1/customers/auth/switch")
    fun switchAccount(@Body switchAccountRequest: SwitchAccountRequest): Call<SwitchAccountResponse>

    @GET("v1/customers/profile")
    fun getProfileDetails(): Call<ProfileDetailsResponse>

    @GET("v1/customers/termsandconditions")
    fun getTnc(): Call<TncResponse>

    @GET("v1/customers/transactions/recent?page=1&limit=9")
    fun getRecentTransactions(): Call<RecentTransactionResponse>

    @GET("v1/customers/transactions/wallet?")
    suspend fun getWalletTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<WalletActivityResponse>

    @POST("v1/customers/wallet/topup")
    fun topupWallet(@Body topupRequest: WalletTopupRequest): Call<WalletTopupResponse>

    @POST("v1/customers/wallet/withdraw")
    fun withdrawFromWallet(
        @Body withdrawRequest: WalletWithdrawRequest
    ): Call<WalletWithdrawResponse>

    @GET("v1/customers/contacts")
    fun getAllContacts(
        @Query("search") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<AllContactsResponse>

    @GET("v1/customers/contacts/{id}")
    fun getContactDetails(@Path("id") id: Int): Call<ContactDetails>

    @POST("v1/customers/payments/initiate")
    fun initPayment(@Body paymentRequest: PaymentRequest): Call<PaymentResponse>

    @POST("v1/customers/payments/transfer")
    fun transferAmount(@Body transferRequest: TransferRequest): Call<TransferResponse>

    @GET("v1/customers/transactions/main/{ref_id}")
    fun getTransactionDetails(@Path("ref_id") refId: String): Call<TransactionDetailsResponse>

    @GET("v1/customers/contacts/{user_id}/transactions")
    suspend fun getTransactionHistory(
        @Path("user_id") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<TransactionHistoryResponse>

    @GET("v1/customers/contacts/{user_id}/transactions")
    fun getTransactionHistoryAsync(
        @Path("user_id") userId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<TransactionHistoryResponse>

    @POST("v1/customers/payments/request")
    fun requestPayment(@Body paymentRequest: PaymentRequest): Call<PaymentResponse>

    @POST("v1/customers/payments/reject")
    fun rejectPayment(@Body paymentApprovalRequest: PaymentApprovalRequest): Call<PaymentApprovalResponse>

    @POST("v1/customers/payments/approve")
    fun acceptPayment(@Body paymentApprovalRequest: PaymentApprovalRequest): Call<PaymentApprovalResponse>

    @GET("v1/customers/transactions/main?")
    fun getTransactionHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("mode") mode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("status") status: String,
        @Query("type") type: String
    ): Call<com.emcash.customerapp.model.transactions.TransactionHistoryResponse>

    @GET("v1/customers/notification")
    fun getNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Call<NotificationResponse>

    @POST("v1/customers/payments/qrcode/check")
    fun getQRResult(
        @Body qrRequest: QRRequest
    ): Call<QRResponse>

    @GET("v1/customers/transactions/wallet/group")
    suspend fun getWalletGropedTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): WalletActivityGroupResponse

    @GET("v1/customers/transactions/main/group?")
    suspend fun getGroupedTransactionHistory(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("mode") mode: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("status") status: String,
        @Query("type") type: String
    ): TransactionHistoryGroupResponse

    @GET("v1/customers/contacts/group")
    suspend fun getGroupedContacts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") search: String
    ): ContactsGroupResponse

    @GET("v1/customers/contacts/{user_id}/transactions/group")
    suspend fun getContactTransactionPagedList(
        @Path("user_id") user_id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): TransactionGroupResponse


    @GET("v1/customers/empay/cards")
    fun getBankCard(
    ): Call<BankCardsListingResponse>

    @POST("v1/customers/empay/payment-through-existing-card")
    fun paymentByExistingCard(
        @Body paymentByExistingCardRequest: PaymentByExistingCardRequest
    ): Call<PaymentByExisitingCardResponse>

    @POST("v1/customers/empay/payment-through-new-card")
    fun paymentByNewCard(
        @Body paymentByNewCardRequest: PaymentByNewCardRequest
    ): Call<PaymentByNewCardResponse>

    @POST("v1/customers/contacts/{user_id}/block")
    fun blockContact(
        @Path("user_id") user_id: Int
    ): Call<BlockedResponse>

    @POST("v1/customers/contacts/{user_id}/unblock")
    fun unBlockContact(
        @Path("user_id") user_id: Int
    ): Call<UnblockedResponse>

    @POST("v1/customers/bank/details")
    fun addBankDetails(
        @Body addBankDetailsRequest: AddBankDetailsRequest
    ): Call<UserBankAccountResponse>


    @PUT("v1/customers/bank/details")
    fun editBankDetails(
        @Body editBankDetailsRequest: EditBankDetailsRequest
    ): Call<UserBankAccountResponse>


    @GET("v1/customers/bank/details")
    fun getBankDetails(
    ): Call<BankDetailsResponse>


    @POST("v1/customers/empay/payer-authentication")
    fun authenticatePayer(
        @Body payerAuthenticatorRequest: PayerAuthenticatorRequest
    ): Call<PayerAuthenticatorResponse>

    @POST("v1/customers/contacts/{user_id}/block")
    suspend fun blockContactAsync(
        @Path("user_id") user_id: Int
    ): BlockedResponse

}