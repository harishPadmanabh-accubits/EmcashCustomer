package com.emcash.customerapp.model.bankCard


import com.google.gson.annotations.SerializedName


data class BankCardsListingResponse(
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
        @SerializedName("cards")
        val cards: List<Card>
    ) {
        data class Card(
            @SerializedName("alias")
            val alias: String,
            @SerializedName("bin")
            val bin: String,
            @SerializedName("isDefault")
            var isDefault: Boolean,
            @SerializedName("last4")
            val last4: String,
            @SerializedName("productName")
            val productName: String,
            @SerializedName("schema")
            val schema: String,
            @SerializedName("state")
            val state: String,
            @SerializedName("token")
            val token: String,
            @SerializedName("type")
            val type: String,
            @SerializedName("verticles")
            val verticles: List<Verticle>
        ) {
            data class Verticle(
                @SerializedName("name")
                val name: String,
                @SerializedName("type")
                val type: Int
            )
        }
    }
}