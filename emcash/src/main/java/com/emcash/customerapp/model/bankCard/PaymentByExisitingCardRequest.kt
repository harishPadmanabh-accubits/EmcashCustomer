package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class PaymentByExisitingCardRequest(
    @SerializedName("amountAuthorized")
    val amount: Amount,
    @SerializedName("billerId")
    val billerId: String,
    @SerializedName("card")
    val card: String?=null,
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("description")
    val description: String?=null,
    @SerializedName("existingCardToken")
    val existingCardToken: String,
    @SerializedName("iban")
    val iban: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("loadEmcash")
    val loadEmcash: Boolean,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("useExistingCard")
    val useExistingCard: Boolean
) {
    data class Amount(
        @SerializedName("currencyCode")
        val currencyCode: String,
        @SerializedName("value")
        val value: String
    )

    data class Customer(
        @SerializedName("id")
        val id: String,
        @SerializedName("idType")
        val idType: Int
    )
}