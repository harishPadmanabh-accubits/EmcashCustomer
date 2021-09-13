package com.emcash.customerapp.model.payments


import com.google.gson.annotations.SerializedName

data class TransactionGroupResponse(
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
        val transactionGroups: List<TransactionGroup>,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("wallet")
        val wallet: Wallet
    ) {
        data class Contact(
            @SerializedName("email")
            val email: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("isContactUserBlockedLoggedInUser")
            val isContactUserBlockedLoggedInUser: Boolean,
            @SerializedName("isLoggedInUserBlockedContactUser")
            val isLoggedInUserBlockedContactUser: Boolean,
            @SerializedName("name")
            val name: String,
            @SerializedName("phoneNumber")
            val phoneNumber: String,
            @SerializedName("ppp")
            val level: Int,
            @SerializedName("profileImage")
            val profileImage: String?,
            @SerializedName("roleId")
            val roleId: Int
        )

        data class TransactionGroup(
            @SerializedName("key")
            val date: String,
            @SerializedName("transactions")
            val transactions: List<Transaction>
        ) {
            data class Transaction(
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
                val iban: String?,
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
            ) {
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
                    val profileImage: String?,
                    @SerializedName("roleId")
                    val roleId: Int,
                    @SerializedName("userId")
                    val userId: Int
                )
            }
        }

        data class Wallet(
            @SerializedName("amount")
            val amount: Int,
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("status")
            val status: Int,
            @SerializedName("updatedAt")
            val updatedAt: String,
            @SerializedName("userId")
            val userId: String,
            @SerializedName("walletAddress")
            val walletAddress: String
        )
    }
}