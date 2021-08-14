package com.emcash.customerapp.model.auth.switchAccount


import com.google.gson.annotations.SerializedName

data class SwitchAccountResponse(
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
        @SerializedName("email")
        val email: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("sessionId")
        val sessionId: String
    )
}