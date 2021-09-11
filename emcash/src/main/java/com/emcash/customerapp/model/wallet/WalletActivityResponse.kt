package com.emcash.customerapp.model.wallet


import com.emcash.customerapp.model.DummyTransactionDetalsModel
import com.emcash.customerapp.model.profile.Wallet
import com.google.gson.annotations.SerializedName

data class WalletActivityResponse(
    @SerializedName("data")
    val data: Data,
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
        val activities: List<WalletActivityItem>,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("wallet")
        val wallet: Wallet
    ){
        data class WalletActivityItem(
            @SerializedName("balance")
            val balance: Int,
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("mode")
            val mode: Int,
            @SerializedName("transactionId")
            val transactionId: String,
            @SerializedName("transactionInfo")
            val transactionInfo: TransactionInfo,
            @SerializedName("updatedAt")
            val updatedAt: String,
            @SerializedName("userId")
            val userId: String,
            @SerializedName("walletId")
            val walletId: String
        )

    }
}



data class TransactionInfo(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("beneficiary")
    val beneficiary: Beneficiary,
    @SerializedName("beneficiaryId")
    val beneficiaryId: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("empayOrderId")
    val empayOrderId: String?,
    @SerializedName("empayTransactionId")
    val empayTransactionId: String?,
    @SerializedName("expiresIn")
    val expiresIn: String?,
    @SerializedName("handShakingStatus")
    val handShakingStatus: Boolean,
    @SerializedName("iban")
    val iban: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("method")
    val method: Int,
    @SerializedName("paymentConfirmedAt")
    val paymentConfirmedAt: String,
    @SerializedName("processId")
    val processId: String,
    @SerializedName("processInfo")
    val processInfo: String,
    @SerializedName("remitter")
    val remitter: Remitter,
    @SerializedName("remitterId")
    val remitterId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: String
)

data class Beneficiary(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("ppp")
    val ppp: Int,
    @SerializedName("profileImage")
    val profileImage: Any?,
    @SerializedName("roleId")
    val roleId: Int
)

data class Remitter(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("ppp")
    val ppp: Int,
    @SerializedName("profileImage")
    val profileImage: Any?,
    @SerializedName("roleId")
    val roleId: Int
)

data class TransactionItemUiModel(
    val date: String,
    val transactionList: List<WalletActivityResponse.Data.WalletActivityItem>
)

data class WalletActivityUIModel(
val walletActivities:List<WalletActivityGroupResponse.Data.WalletActivityGroup>
)


data class WalletActivityGroupResponse(
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
        val walletActivities: List<WalletActivityGroup>,
        @SerializedName("totalPages")
        val totalPages: Int,
        @SerializedName("wallet")
        val wallet: Wallet
    ) {
        data class WalletActivityGroup(
            @SerializedName("key")
            val date: String,
            @SerializedName("transactions")
            val transactions: List<WalletActivity>
        ) {
            data class WalletActivity(
                @SerializedName("balance")
                val balance: Int,
                @SerializedName("beneficiary")
                val beneficiary: Beneficiary,
                @SerializedName("createdAt")
                val createdAt: String,
                @SerializedName("id")
                val id: String,
                @SerializedName("mode")
                val mode: Int,
                @SerializedName("remitter")
                val remitter: Remitter,
                @SerializedName("transactionId")
                val transactionId: String,
                @SerializedName("transactionInfo")
                val transactionInfo: TransactionInfo,
                @SerializedName("updatedAt")
                val updatedAt: String,
                @SerializedName("userId")
                val userId: Int,
                @SerializedName("walletId")
                val walletId: String
            )
        }

    }
}