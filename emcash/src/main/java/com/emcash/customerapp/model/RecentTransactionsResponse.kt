package com.emcash.customerapp.model


import com.google.gson.annotations.SerializedName


data class RecentTransactionsResponse(
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
        val rows: List<Row>,
        @SerializedName("totalPages")
        val totalPages: Int
    ) {
        data class Row(
            @SerializedName("email")
            val email: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("phoneNumber")
            val phoneNumber: String,
            @SerializedName("ppp")
            val ppp: Int,
            @SerializedName("profileImage")
            val profileImage: String?=null,
            @SerializedName("roleId")
            val roleId: Int,
            @SerializedName("userId")
            val userId: Int
        )
    }
}