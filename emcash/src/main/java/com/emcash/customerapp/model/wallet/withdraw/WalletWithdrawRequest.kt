package com.emcash.customerapp.model.wallet.withdraw


import com.google.gson.annotations.SerializedName

data class WalletWithdrawRequest(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("iban")
    val iban: String,
    @SerializedName("description")
    val description: String="",
    @SerializedName("latitude")
    val latitude: Double=0.0,
    @SerializedName("longitude")
    val longitude: Double=0.0
)