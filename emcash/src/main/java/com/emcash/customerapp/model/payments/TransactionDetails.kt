package com.emcash.customerapp.model.payments


import com.google.gson.annotations.SerializedName

data class TransactionDetails(
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
        @SerializedName("amount")
        val amount: Int,
        @SerializedName("beneficiaryId")
        val beneficiaryId: String,
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
        @SerializedName("isReciever")
        val isReciever: Boolean,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("method")
        val method: Int,
        @SerializedName("mode")
        val mode: Int,
        @SerializedName("paymentConfirmedAt")
        val paymentConfirmedAt: String,
        @SerializedName("processId")
        val processId: String,
        @SerializedName("processInfo")
        val processInfo: String,
        @SerializedName("remitterId")
        val remitterId: String,
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
}
data class TransferUserInfo(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("ppp")
    val level: Int,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("userId")
    val userId: Int
)
data class WalletTransactionInfo(
    @SerializedName("balance")
    val balance: Any?,
    @SerializedName("mode")
    val mode: Int,
    @SerializedName("transactionId")
    val transactionId: Any?,
    @SerializedName("userId")
    val userId: Int?,
    @SerializedName("walletId")
    val walletId: Any?,
    @SerializedName("walletTransactionId")
    val walletTransactionId: Any?
)