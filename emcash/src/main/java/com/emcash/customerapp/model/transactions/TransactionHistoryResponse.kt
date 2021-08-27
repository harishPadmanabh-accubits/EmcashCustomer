package com.emcash.customerapp.model.transactions


import com.emcash.customerapp.model.payments.TransferUserInfo
import com.google.gson.annotations.SerializedName

data class TransactionHistoryResponse(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("count")
        val count: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("page")
        val page: Int,
        @SerializedName("rows")
        val transactions: List<Transaction>,
        @SerializedName("totalPages")
        val totalPages: Int
    )
}
data class Transaction(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("beneficiaryId")
    val beneficiaryId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("expiresIn")
    val expiresIn: Any?,
    @SerializedName("handShakingStatus")
    val handShakingStatus: Boolean,
    @SerializedName("iban")
    val iban: Any?,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("method")
    val method: Int,
    @SerializedName("paymentConfirmedAt")
    val paymentConfirmedAt: String?,
    @SerializedName("processId")
    val processId: String,
    @SerializedName("processInfo")
    val processInfo: String?,
    @SerializedName("remitterId")
    val remitterId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("transactionId")
    val transactionId: String,
    @SerializedName("transferUserInfo")
    val transferUserInfo: TransferUserInfo,
    @SerializedName("type")
    val type: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("walletTransactionInfo")
    val walletTransactionInfo: WalletTransactionInfo
)

data class WalletTransactionInfo(
    @SerializedName("balance")
    val balance: Int?,
    @SerializedName("mode")
    val mode: Int?,
    @SerializedName("transactionId")
    val transactionId: String?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("walletId")
    val walletId: String?,
    @SerializedName("walletTransactionId")
    val walletTransactionId: String?
)

data class TransactionHistoryGroupUIModel(
    val date: String,
    val activities: List<Transaction>
)

data class FilterDurationResponse(
    val id:Int, val duration:String
)
