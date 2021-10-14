package com.emcash.customerapp.model.profile


import com.google.gson.annotations.SerializedName

data class ProfileDetailsResponse(
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
        @SerializedName("address")
        val address: Any?,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("customer")
        val customer: Customer,
        @SerializedName("email")
        val email: String,
        @SerializedName("fcmToken")
        val fcmToken: Any?,
        @SerializedName("guid")
        val guid: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("isRegistrationCompleted")
        val isRegistrationCompleted: Boolean,
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("ppp")
        val level: Int,
        @SerializedName("profileImage")
        val profileImage: String?,
        @SerializedName("qrCode")
        val qrCode: Any?,
        @SerializedName("roleId")
        val roleId: Int,
        @SerializedName("status")
        val status: Int,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("wallet")
        val wallet: Wallet,
        @SerializedName("zipCode")
        val zipCode: Any?
    )
}

data class Customer(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("empayId")
    val empayId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("rewardLevel")
    val rewardLevel: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: String
)

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