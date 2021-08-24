package com.emcash.customerapp.model.wallet.topup


import com.google.gson.annotations.SerializedName

data class WalletTopupRequest(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double=0.0,
    @SerializedName("longitude")
    val longitude: Double=0.0
)