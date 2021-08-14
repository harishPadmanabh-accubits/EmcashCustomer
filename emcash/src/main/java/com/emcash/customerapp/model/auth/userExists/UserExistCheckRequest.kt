package com.emcash.customerapp.model.auth.userExists


import com.google.gson.annotations.SerializedName

data class UserExistCheckRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)