package com.emcash.customerapp.model.payments


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
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
        @SerializedName("referenceId")
        val referenceId: String
    )
}