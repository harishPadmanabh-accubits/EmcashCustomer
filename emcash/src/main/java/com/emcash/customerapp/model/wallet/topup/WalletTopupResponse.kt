package com.emcash.customerapp.model.wallet.topup


import com.google.gson.annotations.SerializedName

data class WalletTopupResponse(
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)