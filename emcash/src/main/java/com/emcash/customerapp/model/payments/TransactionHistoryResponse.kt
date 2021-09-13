package com.emcash.customerapp.model.payments


import com.emcash.customerapp.model.contacts.Contact
import com.emcash.customerapp.model.profile.Wallet
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
        @SerializedName("contact")
        val contact: Contact,
        @SerializedName("count")
        val count: Int,
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("page")
        val page: Int,
        @SerializedName("rows")
        val transactions: List<TransactionHistory>,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("wallet")
        val wallet: Wallet
    )
}
data class TransactionHistory(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("beneficiaryId")
    val beneficiaryId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
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
    @SerializedName("isConfirm")
    val isConfirm: Boolean,
    @SerializedName("isReciever")
    val isReciever: Boolean,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
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
    @SerializedName("updatedBy")
    val updatedBy: Int
)

data class TransactionHistoryUI(
    val date:String,
    val transactions:List<TransactionHistory>
)

