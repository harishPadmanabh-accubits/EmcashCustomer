package com.emcash.customerapp.model.auth.switchAccount


import com.google.gson.annotations.SerializedName

data class SwitchAccountRequest(
    @SerializedName("fraction")
    val fraction: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)