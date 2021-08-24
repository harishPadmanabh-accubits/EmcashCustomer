package com.emcash.customerapp.model.payments


import com.google.gson.annotations.SerializedName

data class TransferResponse(
    @SerializedName("data")
    val `data`: Any?,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)