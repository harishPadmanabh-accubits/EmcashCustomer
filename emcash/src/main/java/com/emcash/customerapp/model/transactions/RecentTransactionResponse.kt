package com.emcash.customerapp.model.transactions


import com.google.gson.annotations.SerializedName

data class RecentTransactionResponse(
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
        val transactionList: List<RecentTransactionItem>,
        @SerializedName("totalPages")
        val totalPages: Int
    )
}

data class RecentTransactionItem(
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
