package com.emcash.customerapp.model.payments


import com.google.gson.annotations.SerializedName

data class QRRequest(
    @SerializedName("referenceId")
    val referenceId: String
)


data class QRResponse(
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
        @SerializedName("amount")
        val amount: Int,
        @SerializedName("description")
        val description: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("ppp")
        val level: Int,
        @SerializedName("profileImage")
        val profileImage: Any?,
        @SerializedName("roleId")
        val roleId: Int
    )
}