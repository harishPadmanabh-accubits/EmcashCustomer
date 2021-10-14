package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class PaymentByNewCardResponse(
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
        val transactionId: String,
        @SerializedName("orderId")
        val orderId: String,
        @SerializedName("sessionId")
        val sessionId: String
    ) {

        class PayerAuthentication(
            @SerializedName("url3D")
            val url3D: String,
            @SerializedName("url3DSuccess")
            val url3DSuccess: String,
            @SerializedName("url3DError")
            val url3DError: String

        )
    }
}