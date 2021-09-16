package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class PaymentByNewCardRequest(
    @SerializedName("amountAuthorized")
    val amountAuthorized: AmountAuthorized,
    @SerializedName("billerId")
    val billerId: String,
    @SerializedName("card")
    val card: Card,
    @SerializedName("customer")
    val customer: Customer,
    @SerializedName("description")
    val description: String?=null,
    @SerializedName("existingCardToken")
    val existingCardToken: String?=null,
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
    data class AmountAuthorized(
        @SerializedName("currencyCode")
        val currencyCode: String,
        @SerializedName("value")
        val value: String
    )

    data class Card(
        @SerializedName("cvv")
        val cvv: String,
        @SerializedName("entryMode")
        val entryMode: String,
        @SerializedName("expiryDate")
        val expiryDate: String,
        @SerializedName("holderName")
        val holderName: String,
        @SerializedName("number")
        val number: String,
        @SerializedName("saveAfterTransaction")
        val saveAfterTransaction: Boolean
    )

    data class Customer(
        @SerializedName("id")
        val id: String,
        @SerializedName("idType")
        val idType: Int
    )
}