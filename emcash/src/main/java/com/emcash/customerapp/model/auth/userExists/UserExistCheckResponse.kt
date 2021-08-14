package com.emcash.customerapp.model.auth.userExists


import com.google.gson.annotations.SerializedName

data class UserExistCheckResponse(
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
        @SerializedName("isExists")
        val isExists: Boolean
    )
}