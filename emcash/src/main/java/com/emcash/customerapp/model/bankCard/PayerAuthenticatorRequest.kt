package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class PayerAuthenticatorRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("iban")
    val iban: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("orderId")
    val orderId: String,
    @SerializedName("sessionId")
    val sessionId: String
)