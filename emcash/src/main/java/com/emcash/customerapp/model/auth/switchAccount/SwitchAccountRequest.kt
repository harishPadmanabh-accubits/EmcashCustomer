package com.emcash.customerapp.model.auth.switchAccount


import com.google.gson.annotations.SerializedName

data class SwitchAccountRequest(
    @SerializedName("fraction")
    val fraction: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("fcmToken")
    var fcmToken : String = "50464B84832A00209E3065B6146A99471EAE21613FFAB4D0742693C70978EE31"
)