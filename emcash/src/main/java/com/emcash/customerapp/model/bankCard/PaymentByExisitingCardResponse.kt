package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class PaymentByExisitingCardResponse(
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
        @SerializedName("approvalCode")
        val approvalCode: String,
        @SerializedName("decision")
        val decision: String,
        @SerializedName("payerAuthentication")
        val payerAuthentication: PayerAuthentication,
        @SerializedName("resultDescription")
        val resultDescription: String,
        @SerializedName("transactionId")
        val transactionId: String
    ) {
        class PayerAuthentication(
        )
    }
}