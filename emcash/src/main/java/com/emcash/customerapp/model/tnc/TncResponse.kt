package com.emcash.customerapp.model.tnc


import com.google.gson.annotations.SerializedName

data class TncResponse(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)